package com.devwiki.leafy.global.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatusCode {
    //200 OK
    LOGIN_SUCCESS(HttpStatus.OK,"로그인 성공!"),
    EMAIL_SEND_SUCCESS(HttpStatus.OK,"이메일 발송 성공!"),
    SIGNUP_SUCCESS(HttpStatus.CREATED,"회원 가입 성공!"),
    CRAWL_ARTICLE_SUCCESS(HttpStatus.OK,"공고 크롤링 성공!"),
    HEALTH_CHECK_SUCCESS(HttpStatus.OK,"서버가 정상 작동 중입니다!"),
    EMAIL_VERIFY_CODE_SUCCESS(HttpStatus.OK,"인증 코드 검증 성공!"),
    ANNOUNCE_GET_SUCCESS(HttpStatus.OK,"공고 리스트 불러오기 성공!"),
    FESTIVAL_GET_SUCCESS(HttpStatus.OK,"축제 리스트 불러오기 성공!"),
    SIGNOUT_SUCCESS(HttpStatus.OK,"회원 탈퇴 성공!"),
    CHANGE_NEW_PASSWORD_SUCCESS(HttpStatus.OK,"비밀번호 변경 성공!"),
    CHANGE_MEMBER_INFO_SUCCESS(HttpStatus.OK,"회원 정보 업데이트 완료!"),
    SOS_CREATE_SUCCESS(HttpStatus.OK, "SOS 생성 성공!"),
    SOS_UPDATE_SUCCESS(HttpStatus.OK, "SOS 수정 성공!"),
    SOS_DELETE_SUCCESS(HttpStatus.OK, "SOS 삭제 성공!"),
    SUCCESS_ADD_FAVORITE(HttpStatus.OK, "즐겨찾기 등록 성공!"),
    SUCCESS_DELETE_FAVORITE(HttpStatus.OK, "즐겨찾기 삭제 성공!"),
    SUCCESS_GET_FAVORITE(HttpStatus.OK, "즐겨찾기 불러오기 성공!"),
    SOS_GET_SUCCESS(HttpStatus.OK, "SOS 상세 불러오기 성공!"),
    ARTICLE_GET_SUCCESS(HttpStatus.OK, "기사 목록 가져오기 성공!"),
    NOTICE_GET_SUCCESS(HttpStatus.OK, "공고 목록 불러오기 성공!"),
    IMAGE_UPLOAD_SUCCESS(HttpStatus.OK, "이미지 업로드 성공!"),
    HOME_DATA_LOAD_SUCCESS(HttpStatus.OK,"홈 뷰 데이터 불러오기 성공!"),
    GET_CHAT_SUCEESS(HttpStatus.OK,"챗봇 응답 성공!"),

    ALARM_SETTING_GET_SUCCESS(HttpStatus.OK,"알림 선호 설정 데이터 불러오기 성공!"),
    ALARM_SETTING_PATCH_SUCCESS(HttpStatus.OK,"알림 선호 알림 설정  성공!"),
    ALARM_LIST_GET_SUCCESS(HttpStatus.OK,"알림리스트 불러오기 성공!");

    private final HttpStatus httpStatus;
    private final String message;

}
