package org.unewe.enigma.game.entity;

import javax.persistence.*;

@Entity
@Table(name = "App_Role", uniqueConstraints = {@UniqueConstraint(name = "APP_ROLE_UK", columnNames = "Role_Name")})
public class AppRole {

    @Id
    @GeneratedValue
    @Column(name = "Role_Id", nullable = false)
    private long roleId;

    @Column(name = "Role_name", length = 30, nullable = false)
    private String roleName;

    public AppRole() {
    }



    public AppRole(String roleName) {
        this.roleName = roleName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
