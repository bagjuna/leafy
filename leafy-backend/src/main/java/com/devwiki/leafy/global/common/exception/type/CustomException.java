package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final BadStatusCode badStatusCode;

  public CustomException(BadStatusCode badStatusCode) {
    super(badStatusCode.getMessage());
    this.badStatusCode = badStatusCode;
  }
}
