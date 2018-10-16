package org.unewe.enigma.game.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.unewe.enigma.game.entity.AppRole;
import org.unewe.enigma.game.entity.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class AppRoleDao {

    @Autowired
    private EntityManager entityManager;

    public List<String> getRoles(long userId) {
        String sql = "select ur.appRole.roleName from " + UserRole.class.getName() + " ur where ur.appUser.userId=:userId ";

        Query query = entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        List<String> roles = query.getResultList();
        return roles;
    }
}
