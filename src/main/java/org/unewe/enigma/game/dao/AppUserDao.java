package org.unewe.enigma.game.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.unewe.enigma.game.entity.AppUser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;


@Repository
@Transactional
public class AppUserDao {

    @Autowired
    private EntityManager entityManager;

    public AppUser findUserAccount(String userName) {

        try {
            String sql = "select e from " + AppUser.class.getName() + " e where e.userName=:userName";

            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);

            return (AppUser)query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
