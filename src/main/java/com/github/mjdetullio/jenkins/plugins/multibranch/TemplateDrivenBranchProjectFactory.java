package com.github.mjdetullio.jenkins.plugins.multibranch;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.TopLevelItem;
import hudson.scm.NullSCM;
import jenkins.branch.Branch;
import jenkins.branch.BranchProjectFactory;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Matthew DeTullio
 */
public abstract class TemplateDrivenBranchProjectFactory<P extends AbstractProject<P, B> & TopLevelItem, B extends AbstractBuild<P, B>>
        extends BranchProjectFactory<P, B> {

    private static final String CLASSNAME = TemplateDrivenBranchProjectFactory.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Branch getBranch(@Nonnull P project) {
        return project.getProperty(BranchProjectProperty.class).getBranch();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public P setBranch(@Nonnull P project, @Nonnull Branch branch) {
        BranchProjectProperty property = project.getProperty(BranchProjectProperty.class);

        try {
            if (property == null) {
                project.addProperty(new BranchProjectProperty<P, B>(branch));
            } else if (!property.getBranch().equals(branch)) {
                property.setBranch(branch);
                project.save();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to set BranchProjectProperty", e);
        }

        return project;
    }

    /**
     * Decorates projects by using {@link hudson.model.AbstractItem#updateByXml(Source)} and saving the configuration,
     * rather than only updating the project in memory.
     *
     * @param project the project to decorate
     * @return the project that was just decorated
     */
    @Override
    public P decorate(P project) {
        if (!isProject(project)) {
            return project;
        }

        if (!(getOwner() instanceof TemplateDrivenMultiBranchProject)) {
            throw new IllegalStateException(String.format("%s can only be used with %s.",
                    TemplateDrivenBranchProjectFactory.class.getSimpleName(),
                    TemplateDrivenMultiBranchProject.class.getSimpleName()));
        }

        TemplateDrivenMultiBranchProject<P, B> owner = (TemplateDrivenMultiBranchProject<P, B>) getOwner();

        Branch branch = getBranch(project);
        String displayName = project.getDisplayNameOrNull();
        boolean wasDisabled = project.isDisabled();

        try {
            project.updateByXml((Source) new StreamSource(owner.getTemplate().getConfigFile().readRaw()));

            // Restore settings managed by this plugin
            setBranch(project, branch);
            project.setDisplayName(displayName);
            project.setScm(branch.getScm());

            // Workarounds for JENKINS-21017
            project.setBuildDiscarder(owner.getTemplate().getBuildDiscarder());
            project.setCustomWorkspace(owner.getTemplate().getCustomWorkspace());

            if (!wasDisabled) {
                project.enable();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to update project " + project.getName(), e);
        }

        // TODO Branch.equals needs to compare the properties as well for BuildRetentionBranchProperty to work
        // TODO same goes for comparing SCM if url updates should work
        return super.decorate(project);
    }
}
