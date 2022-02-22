package com.hunter.transactionalannotation.services;

import com.hunter.transactionalannotation.entities.AnotherRecord;
import com.hunter.transactionalannotation.repositories.RecordDataAccess;
import com.hunter.transactionalannotation.entities.Record;
import com.hunter.transactionalannotation.exceptions.MyException;
import com.hunter.transactionalannotation.exceptions.MyUnimportantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    RecordDataAccess recordDataAccess;
    InnerService innerService;

    @Autowired
    public Service(RecordDataAccess recordDataAccess, InnerService innerService) {
        this.recordDataAccess = recordDataAccess;
        this.innerService = innerService;
    }

    /**
     * there is no need to specify a value for rollbackFor, because by default rollback process will be executed for any exception
     */
    @Transactional(rollbackFor = {SQLException.class, MyException.class}, noRollbackFor = MyUnimportantException.class, propagation = Propagation.REQUIRED)
    public void saveRecordInOneTransaction(Record record, int param) throws SQLException, MyException, MyUnimportantException {
        if(param == 1) {
            record.setName(record.getName() + " - A");
            recordDataAccess.save(record);
            throw new SQLException("Throwing sql exception for rollback"); //after SQLException occurred record will not persist because of transactional rollBackFor feature
        }else if(param == 2) {
            record.setName(record.getName() + " - B");
            recordDataAccess.save(record);recordDataAccess.save(record);
            throw new MyException("Throwing my exception for rollback"); //this statement is to prove the second parameter for rollbackFor attribute
        }else if(param == 3) {
            record.setName(record.getName() + " - C");
            recordDataAccess.save(record);
            throw new MyUnimportantException("Throwing my unimportant exception for no rollback"); //transaction manager will ignore this exception and won't rollback process
        }else if(param == 4) {
            record.setName(record.getName() + " - D");
            recordDataAccess.save(record);
            throw new MyException("Throwing my exception for no rollback");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRecordInOneTransactionExperimental(Record record) throws Exception {
        int random = (int) (Math.random() * (11));
        recordDataAccess.save(record);
        try {
            innerService.saveAnotherRecord(new AnotherRecord(0, "another - " + record.getName()));
        }catch (Exception e) {
            System.out.println("Exception was thrown from inner service");
        }finally {
            if(random  > 5) { //this if control is for simulating sql exception from database
                throw new MyException("Throwing my exception from service"); //after MyException occurred the record will not be persisted because of transactional rollBackFor feature
            }
        }
    }

    public void saveRecordWithoutTransaction(Record record) throws SQLException {
        record.setName(record.getName() + " (without transaction annotation)");
        recordDataAccess.save(record);
        throw new SQLException("Throwing exception for rollback"); //even SQLException occurs record will be persisted
    }

    public List<Record> getRecords() {
        return recordDataAccess.findAll();
    }

}
