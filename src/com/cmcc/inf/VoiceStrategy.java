package com.cmcc.inf;

/**
 *
 * Description: 语音转文本策略方法<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver.		Date		Author			Description<br>
 * ------------------------------------------------------<br>
 * 1.0		2018年10月15日 上午9:15:39		zhouqiang		created.<br>
 */

public interface VoiceStrategy {

	public String transformVoice2Text(String audioFilePath);
	
}
