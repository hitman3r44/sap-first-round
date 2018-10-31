package com.h2rd.refactoring.usermanagement.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This a model class of User. It contains the properties of a user.
 *
 * @author Sumit Sarkar
 * @version 1.0
 */

@XmlRootElement
@NoArgsConstructor
@Builder
public class User implements Comparable {

    @NotEmpty
    @Getter
    @Setter
    private String name;

    @NotEmpty
    @Email
    @Getter
    @Setter
    private String email;

    @Size(min = 1)
    @Getter
    @Setter
    private List<String> roles = new ArrayList<String>();

    public User(String email, String name, List<String> roles) {
        this.setName(name);
        this.setEmail(email);
        this.setRoles(roles);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass())
            return false;

        User user = (User) object;

        return new EqualsBuilder()
                .append(email, user.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(email)
                .toHashCode();
    }

    public int compareTo(Object object) {
        User myClass = (User) object;
        return new CompareToBuilder().append(this.email, myClass.email).toComparison();
    }
}
