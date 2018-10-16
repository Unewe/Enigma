package org.unewe.enigma.game.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.unewe.enigma.game.entity.*;
import org.unewe.enigma.game.forms.UserForm;
import org.unewe.enigma.game.repository.EnigmaQuestionRepository;
import org.unewe.enigma.game.repository.EnigmaUserRepository;
import org.unewe.enigma.game.repository.QuestionImgsRepository;
import org.unewe.enigma.game.repository.QuestionTipsRepository;
import org.unewe.enigma.game.utils.EncrytedPasswordUtils;
import org.unewe.enigma.game.utils.WebUtils;

@Controller
public class MainController {

    @Autowired
    private EnigmaUserRepository enigmaUserRepository;
    @Autowired
    private EnigmaQuestionRepository enigmaQuestionRepository;
    @Autowired
    private QuestionTipsRepository questionTipsRepository;
    @Autowired
    private QuestionImgsRepository questionImgsRepository;

    @RequestMapping(value = {"/welcome", "/"}, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Enigma");
        model.addAttribute("message", "Добро пожаловать в Игру!");
        return "welcomePage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {

        return "loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        // After user login successfully.
        String userName = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(userName);

        model.addAttribute("userName", userName + ".");
        if(enigmaUser.getInvite() != null) {
            model.addAttribute("inviteTeam", enigmaUser.getInvite());
        }
        if(enigmaUser.getTeam() != null && enigmaUser.getTeam().equals("Admin")) {
            model.addAttribute("admin", "admin");
        }
        if(enigmaUser.getTeam() == null) {
            model.addAttribute("team", "Вы пока не состоите в команде.");
            model.addAttribute("create", "create");
        } else {
            model.addAttribute("team", "Ваша команда: " + enigmaUser.getTeam() + ".");
            model.addAttribute("exit", "exit");
            ArrayList<String> teammates = new ArrayList<>();

            for(EnigmaUser teammate : enigmaUserRepository.findByTeam(enigmaUser.getTeam())) {
                teammates.add(teammate.getUsername());
            }

            model.addAttribute("teammates", teammates);
        }

        if(enigmaUser.getInvite() != null) {
            model.addAttribute("invite", "Вы получили приглашение в команду " + enigmaUser.getInvite() + ".");
        }

        if(enigmaUser.getLeader() == 1) {
            model.addAttribute("leader", "i'm a leader =)");
        }



//        List<String> teams = enigmaUserRepository.listTeams();
//        teams.remove(null);
//        model.addAttribute("teams", teams);

        return "userInfoPage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationGet(Model model) {
        return "registrationPage";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @Transactional
    public String registrationPost(Model model, @ModelAttribute(value = "username") String username, @ModelAttribute(value = "password") String password) {

        EnigmaUser enigmaUser = new EnigmaUser(username, EncrytedPasswordUtils.encrytePassword(password), 1, 0);

        if(enigmaUserRepository.findByUsername(username) != null) return"redirect:/registration?error=true";

        try {
            enigmaUserRepository.save(enigmaUser);
            return "redirect:/login";
        } catch(Exception e) {
            return "redirect:/registration?error=true";
        }
    }

    @Transactional
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String game(Model model, Principal principal) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);

        int maxQuestion = enigmaQuestionRepository.selectMaxId();

        if(enigmaUser.getTeam() == null) return "errorNoTeam";
        if(enigmaUser.getQuestion() == 0) return "gameNotStarted";
        if(enigmaUser.getQuestion() > maxQuestion) {
            ArrayList<EnigmaUser> list = new ArrayList<>(enigmaUserRepository.findByLeader(1));
            ArrayList<UserForm> formList = new ArrayList<>();

            for(EnigmaUser user : list) {
                if(user.getStartTime() == null) {

                    formList.add( new UserForm(user.getTeam(), "Еще не начал"));
                    model.addAttribute("userlist", formList);
                    return "gameEnded";
                }
                long time =(user.getQuestionTime().getTime() - user.getStartTime().getTime())/60000;
                String stringTime = time/60 + "ч. " + time%60 + "мин.";

                formList.add( new UserForm(user.getTeam(), user.getQuestion() > maxQuestion ? stringTime : "Пока играет."));
            }
            model.addAttribute("userlist", formList);
            return "gameEnded";
        } else {
            Integer questionNumber = enigmaUser.getQuestion();
            EnigmaQuestion question = enigmaQuestionRepository.findById(questionNumber).get();

            model.addAttribute("question", question.getQuestion());
            model.addAttribute("imgs", question.getImgs());

            if(enigmaUser.getStartTime() == null){
                Date startTime = new Date();
                enigmaUserRepository.setBothTimers(startTime, startTime, enigmaUser.getTeam());
                enigmaUser.setQuestionTime(startTime);

            enigmaUser.setStartTime(startTime);
        }
        List<QuestionTips> tips = question.getTips();
            List<QuestionTips> finalTips = new ArrayList<>();
            //Таймер здесь
            long interval =90_000;
            Long sec;
            for(QuestionTips tip : tips) {
                sec = new Date().getTime() - enigmaUser.getQuestionTime().getTime();
                if(sec > interval) {
                    finalTips.add(tip);
                } else{
                    long l = (interval - sec);
                    //Здесь
                    if(l < 90_500)model.addAttribute("sec",String.valueOf(l/1000));

                }
                //И здесь
                interval += 90_000;
            }

            model.addAttribute("tips", finalTips);

        }

        return "gamePage";
    }

    @Transactional
    @RequestMapping(value = "/game", method = RequestMethod.POST)
    public String gamePost(Principal principal, @ModelAttribute(name = "answer") String answer) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        EnigmaQuestion question = enigmaQuestionRepository.findById(enigmaUser.getQuestion()).get();

        String answers = question.getAnswers().toLowerCase();

        ArrayList<String> answersArr = new ArrayList<>();

        while(answers.contains("!")) {
            int index = answers.indexOf("!");
            answersArr.add(answers.substring(0, index).trim());
            answers = answers.substring(index + 1).trim();
        }

        answersArr.add(answers.trim());



        if(answersArr.contains(answer.toLowerCase().replace('ё', 'е'))) {
            //Write
            enigmaUserRepository.setQuestionFor(enigmaUser.getQuestion() + 1, new Date(), enigmaUser.getTeam());
        }
        return "redirect:/game";
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public String getQuestions(Model model) {

        List<EnigmaQuestion> questions = enigmaQuestionRepository.findAll();

        System.out.println(questions.toString());

        model.addAttribute("questions", questions);

        return "questionPage";
    }

    @RequestMapping(value = "/updateQuestion", method = RequestMethod.GET)
    public String updateQuestion(Model model) {

        QuestionInfo question = new QuestionInfo(1, "Введите свой вопрос в это поле", "Ответ", "", "", "","","", "");
        model.addAttribute("question", question);
        return "updateQuestion";
    }

    @Transactional
    @PostMapping(value = "/updateQuestion")
    public String updateQuestionPost(Model model, QuestionInfo question) {

        EnigmaQuestion newQuestion = new EnigmaQuestion(question.getId(), question.getQuestion(), question.getAnswer());

        List<QuestionTips> tips = new ArrayList<>();
        List<QuestionImgs> img = new ArrayList<>();

        if(!question.getTip_1().equals(""))tips.add(new QuestionTips(newQuestion,question.getTip_1()));
        if(!question.getTip_2().equals(""))tips.add(new QuestionTips(newQuestion,question.getTip_2()));
        if(!question.getTip_3().equals(""))tips.add(new QuestionTips(newQuestion,question.getTip_3()));

        if(!question.getImg_1().equals(""))img.add(new QuestionImgs(newQuestion,question.getImg_1()));
        if(!question.getImg_2().equals(""))img.add(new QuestionImgs(newQuestion,question.getImg_2()));
        if(!question.getImg_3().equals(""))img.add(new QuestionImgs(newQuestion,question.getImg_3()));

        newQuestion.setTips(tips);
        //questionImgsRepository.deleteAll();
        newQuestion.setImgs(img);
        enigmaQuestionRepository.save(newQuestion);
        return "redirect:/questions";
    }

    @Transactional
    @RequestMapping(value = "/startTheGame", method = RequestMethod.GET)
    public String startTheGame(Model model) {
        enigmaUserRepository.setQuestionForAll();
        enigmaUserRepository.setQuestionTimeForAll(new Date());

        return "redirect:/game";
    }

    @Transactional
    @GetMapping("/deleteQuestion")
    public String deleteQuestion() {
        enigmaUserRepository.setQuestionForAll();
        enigmaQuestionRepository.deleteById(enigmaQuestionRepository.selectMaxId());

        return "redirect:/questions";
    }

    @Transactional
    @GetMapping("/reset")
    public String reset() {
        enigmaUserRepository.reset();

        return "redirect:/admin";
    }


    @Transactional
    @GetMapping("/test")
    public String test() {

        enigmaUserRepository.setAdminOnly();
        enigmaUserRepository.setQuestionTimeForAdmin(new Date());
        return"redirect:/game";
    }


    @GetMapping("/addTeammate")
    public String addTeammateGet(Model model) {

        ArrayList<EnigmaUser> users = enigmaUserRepository.findByTeam(null);
        model.addAttribute("users", users);

        return"freeUsersList";
    }

    @Transactional
    @PostMapping("/addTeammate")
    @ResponseBody
    public ResponseEntity<?> addTeammatePost(Principal principal, @RequestParam(name = "teammate") String teammate) {
        String username = principal.getName();

        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        EnigmaUser tm = enigmaUserRepository.findByUsername(teammate);
        if(tm.getTeam() == null && tm.getInvite() == null) {
            enigmaUserRepository.inviteUser(enigmaUser.getTeam(), teammate);
            return new ResponseEntity<String>("Игрок " + teammate + " получил приглашение!", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Игрок " + teammate + " уже приглашон в другую команду!", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @GetMapping("/accept")
    public String accept(Principal principal) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        EnigmaUser leader = enigmaUserRepository.findByTeamAndLeader(enigmaUser.getInvite(), 1).get(0);
        enigmaUserRepository.setTeamFor(enigmaUser.getInvite(),leader.getQuestion(), leader.getQuestionTime(), username);

        enigmaUserRepository.setBothTimersForUser(leader.getStartTime(), leader.getQuestionTime(), username);
        return "redirect:/userInfo";
    }

    @Transactional
    @GetMapping("/deny")
    public String deny(Principal principal) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        if(enigmaUser.getLeader() == 1) {
            ArrayList<EnigmaUser> team = enigmaUserRepository.findByTeamAndLeader(enigmaUser.getTeam(), 0);

            if(!team.isEmpty()) {
                enigmaUserRepository.setTeamAndLeaderFor(enigmaUser.getTeam(), 1, team.get(0).getUsername());
            }
            enigmaUserRepository.setTeamAndLeaderFor(null, 0, username);
        } else {
            enigmaUserRepository.setNoTeamFor(null,username);
        }
        return "redirect:/userInfo";
    }

    @Transactional
    @PostMapping("/createTeam")
    public String create(Principal principal, @RequestParam String teamName) {
        String username = principal.getName();
        if(enigmaUserRepository.findByTeamAndLeader(teamName, 1).isEmpty()) {
            System.out.println("OK");
            enigmaUserRepository.setTeamAndLeaderFor(teamName, 1, username);
            enigmaUserRepository.setBothTimersForUser(null, new Date(), username);
            /**
             * Поменять на :
             * enigmaUserRepository.resetQuestionAndTimeFor(0, new Date(), username);
             * после тестирования!
             */
            enigmaUserRepository.resetQuestionAndTimeFor(1, new Date(), username);
            return "redirect:/userInfo";
        }

        return "redirect:/userInfo";
    }


    @GetMapping("/deleteTeammate")
    public String deleteTeammateGet(Principal principal, Model model) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        ArrayList<EnigmaUser> users = enigmaUserRepository.findByTeamAndLeader(enigmaUser.getTeam(), 0);
        model.addAttribute("users", users);
        return"teammatesList";
    }

    @Transactional
    @PostMapping("/deleteTeammate")
    @ResponseBody
    public void deleteTeammatePost(Principal principal, @RequestParam(name = "teammate") String teammate) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        EnigmaUser tm = enigmaUserRepository.findByUsername(teammate);
        if(enigmaUser.getLeader() == 1 && enigmaUser.getTeam().equals(tm.getTeam())) {
            enigmaUserRepository.setNoTeamFor(null, teammate);
            enigmaUserRepository.setBothTimersForUser(null, new Date(), teammate);
        }

    }


    @GetMapping("/changeLeader")
    public String changeLeaderGet(Principal principal, Model model) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        ArrayList<EnigmaUser> users = enigmaUserRepository.findByTeamAndLeader(enigmaUser.getTeam(), 0);
        model.addAttribute("users", users);

        return"changeLeaderList";
    }

    @Transactional
    @PostMapping("/changeLeader")
    @ResponseBody
    public void changeLeaderPost(Principal principal, @RequestParam(name = "teammate") String teammate) {
        String username = principal.getName();
        EnigmaUser enigmaUser = enigmaUserRepository.findByUsername(username);
        EnigmaUser tm = enigmaUserRepository.findByUsername(teammate);
        if(enigmaUser.getLeader() == 1 && enigmaUser.getTeam().equals(tm.getTeam())) {
            enigmaUserRepository.setTeamAndLeaderFor(enigmaUser.getTeam(), 1, teammate);
            enigmaUserRepository.setTeamAndLeaderFor(enigmaUser.getTeam(), 0, username);

        }

    }
}
