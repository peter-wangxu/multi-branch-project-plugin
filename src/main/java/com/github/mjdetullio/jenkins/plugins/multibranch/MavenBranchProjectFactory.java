package com.github.mjdetullio.jenkins.plugins.multibranch;

import hudson.Extension;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.Item;
import jenkins.branch.Branch;
import jenkins.branch.BranchProjectFactoryDescriptor;
import jenkins.branch.MultiBranchProject;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Matthew DeTullio
 */
public final class MavenBranchProjectFactory
        extends TemplateDrivenBranchProjectFactory<MavenModuleSet, MavenModuleSetBuild> {

    /**
     * No-op constructor used for data binding.
     */
    @DataBoundConstructor
    public MavenBranchProjectFactory() {
        // No-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MavenModuleSet newInstance(Branch branch) {
        MavenModuleSet project = new MavenModuleSet(getOwner(), branch.getEncodedName());
        setBranch(project, branch);
        return project;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProject(Item item) {
        return item instanceof MavenModuleSet &&
                ((MavenModuleSet) item).getProperty(BranchProjectProperty.class) != null;
    }

    /**
     * {@link MavenBranchProjectFactory}'s descriptor.
     */
    @SuppressWarnings("unused")
    @Extension
    public static class DescriptorImpl extends BranchProjectFactoryDescriptor {
        @Override
        public String getDisplayName() {
            return "Fixed configuration";
        }

        @Override
        public boolean isApplicable(Class<? extends MultiBranchProject> clazz) {
            return MavenMultiBranchProject.class.isAssignableFrom(clazz);
        }
    }
}
