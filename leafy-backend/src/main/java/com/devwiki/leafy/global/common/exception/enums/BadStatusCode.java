package com.devwiki.leafy.global.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadStatusCode {
	//400 BAD REQUEST
	VERIFICATION_FAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
	INVALID_ACCESS_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
	INVALID_AUTHORIZATION_HEADER_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 헤더가 유효하지 않습니다"),
	INVALID_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "파라미터를 잘못 입력 하셨습니다."),
	EMPTY_SIGNUP_INFO_EXCEPTION(HttpStatus.BAD_REQUEST, "회원가입 요청 정보가 비었습니다."),
	INVALID_MINOR_NAME_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 업종 이름 입니다."),
	//401 UNAUTHORIZED
	EXISTING_EMAIL_EXCEPTION(HttpStatus.UNAUTHORIZED, "이미 존재하는 이메일 입니다."),
	INVALID_LOGIN_STATE_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인 정보가 유효하지 않습니다."),
	TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
	LOGIN_FAILURE_EXCEPTION(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호를 잘못 입력 했습니다."),
	INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰의 서명이 유효하지 않습니다."),
	JWT_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "JWT 인증에 실패)."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
	// 403 Forbidden (주로 인가)
	INSUFFICIENT_PERMISSION_EXCEPTION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "채팅방에 속해 있지 않습니다"),
	// 404 Not Found
	ANNOUNCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공고번호 입니다."),
	USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
	USERID_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 이메일의 유저가 존재하지 않습니다"),
	// 5xx(Server Error)
	INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
	DATABASE_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "DB 처리 중 오류가 발생했습니다."),
	PASSWORD_ENCODING_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 암호화 처리 중 오류가 발생했습니다."),
	FAIL_TO_REGISTER_MEMBER_AUTH_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "회원 권한 등록에 실패했습니다."),
	FAIL_TO_SAVE_VERIFICATION_CODE_REDIS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에 인증번호 저장을 실패 했습니다."),
	FAIL_TO_HANDLE_VERIFICATION_CODE_REDIS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,
		"Redis에서 인증번호 조회/삭제 중 오류 발생 했습니다."),
	REDIS_INVALID_DATA_FORMAT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 데이터 형식 오류가 발생 했습니다."),
	KAKAO_TOKEN_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 토큰 응답이 비어있습니다."),
	KAKAO_TOKEN_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 토큰 요청 중 오류가 발생 했습니다"),
	KAKAO_USERINFO_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 정보 응답이 비어있습니다."),
	KAKAO_USERINFO_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 정보 요청 중 오류가 발생 했습니다."),
	FAIL_TO_COMMUNICATE_KAKAO_OAUTH_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 OAuth 통신 오류가 발생 했습니다"),
	FAIL_TO_PROCESSING_KAKAO_OAUTH_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 OAuth 로그인 과정 중 오류가 발생 했습니다"),
	FAIL_TO_PROCESSING_NOTICE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "공지 정보를 불러오는 과정 중 오류가 발생 했습니다."),
	FAIL_TO_PROCESSING_FILE_UPLOAD_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 알 수 없는 오류가 발생 했습니다."),
	FILE_UPLOAD_FAILED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패 했습니다."),
	FAIL_TO_SEND_ALARM(HttpStatus.INTERNAL_SERVER_ERROR, "사용자가 수신거부한 알림입니다."),
	FAIL_TO_SEND_ALARM2(HttpStatus.INTERNAL_SERVER_ERROR, "사용자가 수신거부한 시간대 입니다"),
	FAILT_TO_GET_RANKING_ANNOUNCE(HttpStatus.INTERNAL_SERVER_ERROR, "인기 공고 불러오는데 실패 했습니다."),
	FILE_DELETE_FAILED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
	// 크롤링 관련 5xx
	CRAWL_URL_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "크롤링 URL 관련 오류"),
	CRAW_IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "크롤링 IO 오류"),
	CRAWL_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "공고 크롤링 실패"),
	ANNOUNCE_DETAIL_GET_FAIL(HttpStatus.NOT_FOUND, "상세 공고 불러오기 실패"),
	// 채팅 관련 오류
	CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방"),
	CHAT_SOS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 SOS"),
	CHAT_ROOM_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 생성 실패"),
	CHAT_MESSAGE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 저장 실패"),
	CHAT_ROOM_NOT_PARTICIPANT(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 참여자가 아님"),
	CHATROOM_NOT_FOUND_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방을 찾을 수 없습니다."),
	CHATROOM_NOT_FOUND_EXCEPTION2(HttpStatus.INTERNAL_SERVER_ERROR, "상세 조회에서 채팅방을 찾을 수 없습니다."),
	SOS_NOT_FOUND_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SOS를 찾을 수 없습니다."),
	FORBIDDEN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "권한이 없습니다."),
	ALREADY_COMPLETED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "이미 완료된 SOS입니다.")
	;
	private final HttpStatus httpStatus;
	private final String message;
}
