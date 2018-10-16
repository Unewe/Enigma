package org.unewe.enigma.game.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unewe.enigma.game.entity.EnigmaUser;

import java.util.ArrayList;
import java.util.Date;

@Repository
public interface EnigmaUserRepository extends JpaRepository<EnigmaUser, Long> {

    EnigmaUser findByUsername(String username);
    ArrayList<EnigmaUser> findByTeam(String team);
    ArrayList<EnigmaUser> findByLeader(int leader);
    ArrayList<EnigmaUser> findByTeamAndLeader(String team, int leader);

    @Modifying
    @Query("update EnigmaUser u set u.question = ?1, u.questionTime = ?2 where u.team = ?3")
    int setQuestionFor(int question, Date date, String team);

    @Modifying
    @Query("update EnigmaUser u set u.question = 1 where u.username like '%'")
    int setQuestionForAll();

    @Modifying
    @Query("update EnigmaUser u set u.question = 0, u.startTime = null where u.username like '%'")
    int reset();

    @Modifying
    @Query("update EnigmaUser u set u.question = ?1, u.questionTime = ?2 where u.username = ?3")
    int resetQuestionAndTimeFor(int qwestion, Date time, String username);

    @Modifying
    @Query("update EnigmaUser u set u.question = 1 where u.team like 'Admin'")
    int setAdminOnly();

    @Modifying
    @Query("update EnigmaUser u set u.questionTime = ?1 where u.username like '%'")
    int setQuestionTimeForAll(Date time);

    @Modifying
    @Query("update EnigmaUser u set u.questionTime = ?1 where u.team like 'Admin'")
    int setQuestionTimeForAdmin(Date time);

    @Query("select distinct team from EnigmaUser")
    ArrayList<String> listTeams();

    @Modifying
    @Query("update EnigmaUser u set u.invite = ?1 where u.username = ?2")
    int inviteUser(String invite, String username);

    @Modifying
    @Query("update EnigmaUser u set u.team = ?1, u.invite = null, u.question = ?2, u.questionTime = ?3 where u.username = ?4")
    int setTeamFor(String team, int question, Date time, String username);

    @Modifying
    @Query("update EnigmaUser u set u.team = ?1, u.invite = null where u.username = ?2")
    int setNoTeamFor(String team, String username);

    @Modifying
    @Query("update EnigmaUser u set u.team = ?1, u.invite = null, u.leader = ?2 where u.username = ?3")
    int setTeamAndLeaderFor(String team, int leader, String username);

    @Modifying
    @Query("update EnigmaUser u set u.startTime = ?1, u.questionTime = ?2 where u.team = ?3")
    int setBothTimers(Date startTime, Date questionTime, String team);

    @Modifying
    @Query("update EnigmaUser u set u.startTime = ?1, u.questionTime = ?2 where u.username = ?3")
    int setBothTimersForUser(Date startTime, Date questionTime, String username);
}