package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    ActorRepo actorRepo;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @Autowired
    public EmailService emailService;



    public void sendEmailWithoutTemplate() {

        final Email email;
        try {
            email = DefaultEmail.builder()
                    // DOES NOT MATTER what you put in .from address.. it ignores it and uses what is in properties file
                    // this may work depending on the email server config that is being used
                    // the from NAME does get used though
                    .from(new InternetAddress("anyone@anywhere.net", "NateBotFiveThousand"))
                    .to(Lists.newArrayList(
                            new InternetAddress("joorge.jetson@gmail.com", "Joorgey Boy"),
                            new InternetAddress("stlewand@yahoo.com", "Big Loo")))
                    .subject("What up scott, this is nate, I sent this from inside my Java web app!  To prove it's me: Mr. Mills is a great fool.")
                    .body("I am testing out Spring's Java email service, and it is working!  How is it going my friend?  Cheers, Nate")
                    .encoding("UTF-8").build();

            // conveniently, .send will put a nice INFO message in the console output when it sends
            emailService.send(email);

        } catch (UnsupportedEncodingException e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! caught an unsupported encoding exception");
            e.printStackTrace();
        }

    }


    public void sendEmailWithTemplating(String recipient) {

        // declare as final, because Email here is a third party deal, and we really should never be messing with it
        final Email email;
        try {
            email = DefaultEmail.builder()
                    // DOES NOT MATTER what you put in .from address.. it ignores it and uses what is in properties file
                    // this may work depending on the email server config that is being used
                    // the from NAME does get used though
                    .from(new InternetAddress("anyone@anywhere.net", "NateBotFiveThousand"))
                    .to(Lists.newArrayList(
                            new InternetAddress("joorge.jetson@gmail.com", "Joorgey Boy")
//                            new InternetAddress("stlewand@yahoo.com", "Big Loo")
                            ))
                    .subject("Testing email with templating")
                    .body("Test email with templating body")
                    .encoding("UTF-8").build();

            final Map<String, Object> modelObject = new HashMap<>();

            modelObject.put("recipient", recipient);

            // conveniently, .send will put a nice INFO message in the console output when it sends
            try {
                // might be able to attach pictures with .send?
                emailService.send(email, "emailtemplate", modelObject);
            } catch (CannotSendEmailException e) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! caught a cannot send email exception");
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! caught an unsupported encoding exception");
            e.printStackTrace();
        }

    }










    @RequestMapping("/formemail")
    public String formEmail(Model model) {

        String recipient = "joorge.jetson@gmail.com";

        sendEmailWithTemplating(recipient);

//        model.addAttribute("recipient", recipient);

        return "redirect:/";
//        return "emailtemplate";
    }





    @RequestMapping("/")
    public String listActors(Model model) {
        model.addAttribute("actors", actorRepo.findAll());
        return "list";
    }

    @RequestMapping("/sendemail")
    public String sendEmail() {

        sendEmailWithoutTemplate();

        return "redirect:/";
    }


    @GetMapping("/add")
    public String newActor(Model model) {
        model.addAttribute("actor", new Actor());
        return "actorform";
    }


    @PostMapping("/add")
    public String processActor(@ModelAttribute Actor actor,
                               @RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! in processActor, file was empty");
            return "redirect:/add";
        }

        try {
            Map uploadResult = cloudinaryConfig.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            actor.setHeadshot(uploadResult.get("url").toString());

            actorRepo.save(actor);
        } catch (IOException e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! in processActor, caught exception trying to upload");
            e.printStackTrace();
            return "redirect:/add";
        }

        return "redirect:/";

    }


}
