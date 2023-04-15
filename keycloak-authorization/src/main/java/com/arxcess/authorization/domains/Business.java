package com.arxcess.authorization.domains;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Business extends PanacheEntity {

    @Column(nullable = false)
    protected String name;

    @Nationalized
    protected String displayName;

    protected String url;

    @Column(nullable = false)
    protected String realmId;

    protected String createdBy;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(String displayName) {

        this.displayName = displayName;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getRealmId() {

        return realmId;
    }

    public void setRealmId(String realmId) {

        this.realmId = realmId;
    }

    public String getCreatedBy() {

        return createdBy;
    }

    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;
    }

}
