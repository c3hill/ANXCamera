package com.xiaomi.player.enums;

public enum PlayerPlaybackState {
    PlayerPlaybackStateIdle(0),
    PlayerPlaybackStatePlaying(1),
    PlayerPlaybackStatePaused(2),
    PlayerPlaybackStateResumed(3),
    PlayerPlaybackStateStarted(4),
    PlayerPlaybackStateStoped(5),
    PlayerPlaybackStateBuffering(6),
    PlayerPlaybackStateEnded(7);
    
    private int nCode;

    private PlayerPlaybackState(int i) {
        this.nCode = i;
    }

    public static PlayerPlaybackState int2enum(int i) {
        PlayerPlaybackState[] values;
        PlayerPlaybackState playerPlaybackState = PlayerPlaybackStateIdle;
        for (PlayerPlaybackState playerPlaybackState2 : values()) {
            if (playerPlaybackState2.ordinal() == i) {
                playerPlaybackState = playerPlaybackState2;
            }
        }
        return playerPlaybackState;
    }

    public String toString() {
        return String.valueOf(this.nCode);
    }
}
