package com.hunter.transactionalannotation.controllers;

import com.hunter.transactionalannotation.entities.Record;
import com.hunter.transactionalannotation.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/transactionalAnnotation")
public class Controller {

    Service service;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    @PostMapping("/saveRecord")
    public void saveRecord(@RequestBody Record record) {
            for (int i = 1; i < 5; i++) {
                try {
                    Record temp = new Record();
                    temp.setName(record.getName());
                    service.saveRecordInOneTransaction(temp, i);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
    }

    @PostMapping("/saveRecordExperimental")
    public void saveRecordExperimental(@RequestBody Record record) {
        try {
            service.saveRecordInOneTransactionExperimental(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/saveRecordWithoutTransactionAnnotation")
    public void saveRecordWithoutTransactionAnnotation(@RequestBody Record record) {
        try {
            service.saveRecordWithoutTransaction(record);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
