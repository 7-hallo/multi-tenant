package com.sevenhallo.multitenant.config.multitenant.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "DataSourceConfig")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfig implements Serializable {
    private static final long serialVersionUID = 5104181924076372196L;
    @Id
    //@GeneratedValue
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private boolean initialize;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataSourceConfig{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", driverClassName='").append(driverClassName).append('\'');
        sb.append(", initialize=").append(initialize);
        sb.append('}');
        return sb.toString();
    }
}
