package com.github.mjdetullio.jenkins.plugins.multibranch;

import hudson.Extension;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Item;
import jenkins.branch.Branch;
import jenkins.branch.BranchProjectFactoryDescriptor;
import jenkins.branch.MultiBranchProject;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Matthew DeTullio
 */
public final class FreeStyleBranchProjectFactory
        extends TemplateDrivenBranchProjectFactory<FreeStyleProject, FreeStyleBuild> {

    /**
     * No-op constructor used for data binding.
     */
    @DataBoundConstructor
    public FreeStyleBranchProjectFactory() {
        // No-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FreeStyleProject newInstance(Branch branch) {
        FreeStyleProject project = new FreeStyleProject(getOwner(), branch.getEncodedName());
        setBranch(project, branch);
        return project;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProject(Item item) {
        return item instanceof FreeStyleProject &&
                ((FreeStyleProject) item).getProperty(BranchProjectProperty.class) != null;
    }

    /**
     * {@link TemplateDrivenBranchProjectFactory}'s descriptor.
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
            return FreeStyleMultiBranchProject.class.isAssignableFrom(clazz);
        }
    }
}
