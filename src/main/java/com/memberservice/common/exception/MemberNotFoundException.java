package com.memberservice.common.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Long memberId) {
        super("해당 멤버를 찾을 수 없습니다: " + memberId);
    }
}
