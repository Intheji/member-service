package com.memberservice.common.exception;

public class ProfileImageNotFoundException extends RuntimeException {

    public ProfileImageNotFoundException(Long memberId) {
        super("해당 멤버의 프로필 이미지가 없습니다. " + memberId);
    }
}
