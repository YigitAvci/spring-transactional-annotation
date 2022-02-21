package com.hunter.transactionalannotation.services;

import com.hunter.transactionalannotation.entities.AnotherRecord;
import com.hunter.transactionalannotation.exceptions.MyException;
import com.hunter.transactionalannotation.repositories.AnotherRecordDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InnerService {

    AnotherRecordDataAccess anotherRecordDataAccess;

    @Autowired
    public InnerService(AnotherRecordDataAccess anotherRecordDataAccess) {
        this.anotherRecordDataAccess = anotherRecordDataAccess;
    }

    /**
     *
     * - PROPAGATION -
     * Propagation describes our business logic's transaction boundary.
     *
     * @REQUIRED: If there is already a transaction, it doesn't need a new one. But if there is not, so it creates a new transaction
     * @REQUIRES_NEW: It always needs a new transaction
     * @SUPPORTS: If a transaction exists, then the existing transaction will be used. If there isn't a transaction, it is executed non-transactional
     * @NOT_SUPPORTED: If a current transaction exists, first Spring suspends it, and then the business logic is executed without a transaction
     * @MANDATORY: If there is an active transaction, then it will be used. If there isn't an active transaction, then Spring throws an exception
     * @NEVER: Spring throws an exception if there's an active transaction
     * @NESTED: Spring checks if a transaction exists, and if so, it marks a save point. This means that if our business logic execution throws an exception, then the transaction <rollbacks to this save point. If there's no active transaction, it works like REQUIRED
     *
     * - Side Effects By Concurrent Transactions -
     * @dirty_read: read the uncommitted change of a concurrent transaction
     * @nonrepeatable_read: get different value on re-read of a row if a concurrent transaction updates the same row and commits
     * @phantom_read: get different rows after re-execution of a range query if another transaction adds or removes some rows in the range and commits
     *
     * - ISOLATION -
     * Isolation describes how changes applied by concurrent transactions are visible to each other
     *
     * @READ_UNCOMMITTED: It reads the uncommitted changes of another transaction. All three side effects can happen
     * @READ_COMMITTED: It reads only committed changes that another transaction made. So it does not allow dirty read to occur
     * @REPEATABLE_READ: It prevents dirty and non-repeatable reads on a row. It never gets different value for the same row in the same transaction. But if it comes to range-queries, it may get newly added and removed rows.
     * @SERIALIZABLE: It prevents all three side effect that we mentioned before. But it has the lowest concurrent access rate because it executes concurrent calls sequentially.
     *
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAnotherRecord(AnotherRecord anotherRecord) throws MyException {
        anotherRecordDataAccess.save(anotherRecord);
        int random = (int) (Math.random() * (11));
        if(random > 5) {
            throw new MyException("Throwing my exception from - INNER - service");
        }
    }
}
