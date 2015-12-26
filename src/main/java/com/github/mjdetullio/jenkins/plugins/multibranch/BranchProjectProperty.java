package com.github.mjdetullio.jenkins.plugins.multibranch;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.TopLevelItem;
import jenkins.branch.Branch;

import javax.annotation.Nonnull;

/**
 * A basic {@link JobProperty} that holds a {@link Branch} so that {@link TemplateDrivenMultiBranchProject}s can
 * manage the project holding this property.
 *
 * @author Matthew DeTullio
 */
public class BranchProjectProperty<P extends AbstractProject<P, B> & TopLevelItem, B extends AbstractBuild<P, B>>
        extends JobProperty<P> {

    private Branch branch;

    /**
     * Creates a new property with the Branch it will hold.
     *
     * @param branch the branch
     */
    public BranchProjectProperty(@Nonnull Branch branch) {
        this.branch = branch;
    }

    /**
     * Gets the Branch held by the property.
     *
     * @return the branch
     */
    @Nonnull
    public Branch getBranch() {
        return branch;
    }

    /**
     * Sets the Branch held by the property.
     *
     * @param branch the branch
     */
    public void setBranch(@Nonnull Branch branch) {
        this.branch = branch;
    }

    /**
     * {@link BranchProjectProperty}'s descriptor.
     */
    @SuppressWarnings("unused")
    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return "Branch";
        }
    }
}
