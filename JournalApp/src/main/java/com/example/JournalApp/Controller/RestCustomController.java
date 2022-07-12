package com.example.JournalApp.Controller;

import com.example.JournalApp.Model.JournalEntry;
import com.example.JournalApp.Model.User;
import com.example.JournalApp.Service.CustomUserDetailService;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RestCustomController {

    @Autowired
    private CustomUserDetailService userDetailService;



    @GetMapping("/getUserPortal")
    public ResponseEntity<Object> getUserPortal() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getEntries")
    public ResponseEntity<Object> getEntries() {
        User user = userDetailService.getAuthenticatedUser();
        return ResponseEntity.ok().body(user.getEntries());

    }

    @PostMapping("/addEntry")
    public void addEntry(@RequestBody String string) throws ParseException {

        JSONObject jsonObject = new JSONObject(string);

        String title = jsonObject.getString("title");
        String entry = jsonObject.getString("entry");

        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle(title);
        newEntry.setEntry(entry);

        userDetailService.addEntry(newEntry);
    }

    @RequestMapping("/removeEntry/{entryId}")
    public ResponseEntity<Object> removeEntry(@PathVariable Long entryId){

        User user = userDetailService.getAuthenticatedUser();
        userDetailService.removeEntry(user.getUser_id(), entryId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getEntry/{entryId}")
    public ResponseEntity<Object> getEntry(@PathVariable Long entryId) {
        User user = userDetailService.getAuthenticatedUser();
        return ResponseEntity.ok().body(userDetailService.getEntry(user.getUser_id(),entryId));
    }

    @RequestMapping("/updateEntry/{entryId}")
    public void updateEntry(@PathVariable Long entryId,@RequestBody String requestBody){

        JSONObject jsonObject = new JSONObject(requestBody);

        String title = jsonObject.getString("title");
        String entry = jsonObject.getString("entry");

        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle(title);
        newEntry.setEntry(entry);

        User user = userDetailService.getAuthenticatedUser();
        userDetailService.updateEntry(user.getUser_id(),entryId,newEntry);

    }

}
