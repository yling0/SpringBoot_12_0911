package me.ling.springboot_12_0911.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import me.ling.springboot_12_0911.configs.CloudinaryConfig;
import me.ling.springboot_12_0911.models.Actor;
import me.ling.springboot_12_0911.repositories.ActorRepo;
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
public class HomeController {

    @Autowired
    public EmailService emailService;

    @Autowired
    ActorRepo actorRepo;

    @Autowired
    CloudinaryConfig cloudinaryc;

    @RequestMapping("/")
    public String listActors(Model model)
    {
        model.addAttribute("actors", actorRepo.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String newActor (Model model)
    {
        model.addAttribute("actor", new Actor());
        return "form";
    }

    @PostMapping("/add")
    public String processActor(@ModelAttribute Actor actor, @RequestParam("file")MultipartFile file)
    {
        if (file.isEmpty())
        {
            return "redirect:/add";
        }
        try
        {
            Map uploadResult = cloudinaryc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            actor.setHeadshot(uploadResult.get("url").toString());
            actorRepo.save(actor);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "redirect:/add";
        }

        return "redirect:/";
    }


    public void sendEmailWithTemplating(String recipient) throws UnsupportedEncodingException, CannotSendEmailException
    {
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("javacodingtesting@gmail.com", "Marco Tullio Cicerone"))
                .to(Lists.newArrayList(new InternetAddress("xyun2016@gmail.com", "Pomponius Atticus")))
                .subject("Laelius de amicitia")
                .body("Firmagwepogijw gwejiogpjw gjwiogjwg gpweo ei, geiros, fides est.")
                .encoding("UTF-8").build();
        final Map<String, Object> modelObject = new HashMap<>();
        modelObject.put("recipient", recipient);
        emailService.send(email,"emailtemp",modelObject);

        System.out.println(email);
        System.out.println(recipient);
        System.out.println(emailService);

    }






}
