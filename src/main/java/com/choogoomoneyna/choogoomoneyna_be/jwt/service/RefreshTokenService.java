package com.choogoomoneyna.choogoomoneyna_be.jwt.service;

public interface RefreshTokenService {

    /**
     * 사용자의 리프레시 토큰을 생성하고 저장합니다.
     *
     * @param userId   사용자 ID
     * @param nickname 사용자 닉네임
     * @return 생성된 리프레시 토큰
     */
    public String generateRefreshTokenAndSave(Long userId, String nickname);

    /**
     * 특정 사용자의 모든 리프레시 토큰을 삭제합니다.
     *
     * @param userId 사용자 ID
     */
    public void deleteAllTokensByUserId(Long userId);

    /**
     * 특정 리프레시 토큰을 삭제합니다.
     *
     * @param refreshToken 삭제할 리프레시 토큰
     */
    public void deleteTokenByRefreshToken(String refreshToken);

    /**
     * 리프레시 토큰의 만료 여부를 확인합니다.
     *
     * @param refreshToken 확인할 리프레시 토큰
     * @return 만료 여부 (true: 만료됨, false: 유효함)
     */
    public boolean isRefreshTokenExpired(String refreshToken);

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 새로 발급된 액세스 토큰
     */
    public String reissueAccessToken(String refreshToken);
}
