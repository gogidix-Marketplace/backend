package com.gogidix.transaction.audit.domain.model;

/**
 * Enumeration of standard audit actions.
 */
public enum AuditAction {
    CREATE,
    READ,
    UPDATE,
    DELETE,
    EXECUTE,
    COMPENSATE,
    COMPLETE,
    FAIL,
    RETRY,
    CANCEL,
    APPROVE,
    REJECT,
    SUBMIT,
    WITHDRAW,
    AUTHENTICATE,
    AUTHORIZE,
    LOGOUT,
    LOGIN,
    EXPORT,
    IMPORT,
    BULK_CREATE,
    BULK_UPDATE,
    BULK_DELETE,
    STATUS_CHANGE,
    STATE_TRANSITION,
    VALIDATE,
    PROCESS,
    NOTIFY
}
