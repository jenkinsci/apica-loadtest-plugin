package com.apica.apicaloadtest.environment;

import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

public abstract class LoadtestEnvironment implements ExtensionPoint, Describable<LoadtestEnvironment>
{

    private String shortName;
    private String displayName;

    public LoadtestEnvironment(String shortName, String displayName)
    {
        this.displayName = displayName;
        this.shortName = shortName;
    }

    public Descriptor<LoadtestEnvironment> getDescriptor()
    {
        return Jenkins.getInstance().getDescriptor(getClass());
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    public abstract String getLtpWebServiceBaseUrl();
    public abstract String getLtpWebServiceVersion();
    public abstract boolean isMatch(String shortName);
}
