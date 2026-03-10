package com.memberservice.common.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Long memberId) {
        super("Member not found: " + memberId);
    }
}
