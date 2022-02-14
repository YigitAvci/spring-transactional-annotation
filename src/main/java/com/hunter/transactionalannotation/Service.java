package com.hunter.transactionalannotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

@org.springframework.stereotype.Service
public class Service {

    @PersistenceContext
    private EntityManager em;

    @Transactional(rollbackFor = {SQLException.class, MyException.class}, noRollbackFor = MyUnimportantException.class)
    public void saveRecordInOneTransaction(Record record, int param) throws SQLException, MyException, MyUnimportantException {
        if(param == 1) {
            record.setName(record.getName() + "A");
            em.persist(record);
            throw new SQLException("Throwing sql exception for rollback"); //after SQLException occurred record will not persist because of transactional rollBackFor feature
        }else if(param == 2) {
            record.setName(record.getName() + "B");
            em.persist(record);
            throw new MyException("Throwing my exception for rollback"); //this statement is to prove the second parameter for rollbackFor attribute
        }else if(param == 3) {
            record.setName(record.getName() + "C");
            em.persist(record);
            throw new MyUnimportantException("Throwing my unimportant exception for no rollback"); //transaction manager will ignore this exception and won't rollback process
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void saveRecordInOneTransactionExperimental(Record record) throws SQLException {
        int random = (int) (Math.random() * (10 + 1));
        em.persist(record);
        if(random  > 5) { //this if control is for simulating sql exception from database
            throw new SQLException("Throwing sql exception for rollback"); //after SQLException occurred the record will not be persisted because of transactional rollBackFor feature
        }
    }

    public void saveRecordWithoutTransaction(Record record) throws SQLException {
        record.setName(record.getName() + " (without transaction annotation)");
        em.persist(record);
        throw new SQLException("Throwing exception for rollback"); //even SQLException occurs record will be persisted
    }

}
