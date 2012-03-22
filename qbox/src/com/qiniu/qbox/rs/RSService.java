package com.qiniu.qbox.rs;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

import com.qiniu.qbox.Config;
import com.qiniu.qbox.oauth2.AuthException;
import com.qiniu.qbox.oauth2.CallRet;
import com.qiniu.qbox.oauth2.Client;

public class RSService {

	private Client conn;
	private String tableName;
	private BASE64Encoder encoder = new BASE64Encoder();

	public RSService(Client conn, String tblName) {
		this.conn = conn;
		this.tableName = tblName;
	}

	// func PutAuth() => PutAuthRet
	// �ϴ���Ȩ������һ��������Ч�Ŀ������ϴ�URL��
	public PutAuthRet putAuth() throws RSException {
		CallRet callRet = null;
		try {
			callRet = conn.call(Config.IO_HOST + "/put-auth/", 1, 3);
			if (callRet.ok()) {
				return new PutAuthRet(new JSONObject(callRet.getResult()));
			} else {
				return new PutAuthRet(callRet);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RSException("Response is not in valid JSON format.", e);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func Get(key string, attName string) => (data GetRet, code int, err
	 * Error) ������Ȩ������һ��������Ч�Ŀ���������URL��
	 */
	public GetRet get(String key, String attName) throws RSException {
		String entryURI = this.tableName + ":" + key;
		String url = Config.RS_HOST + "/get/" + encode(entryURI) + "/attName/"
				+ encode(attName);
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			if (callRet.ok()) {
				return new GetRet(new JSONObject(callRet.getResult()));
			} else {
				return new GetRet(callRet);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RSException("Response is not in valid JSON format.", e);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func GetIfNotModified(key string, attName string, base string) => (data
	 * GetRet, code int, err Error)
	 * ������Ȩ������һ��������Ч�Ŀ���������URL�������������ļ�û�����޸ĵĻ������ڶϵ�������
	 */
	public GetRet getIfNotModified(String key, String attName, String base)
			throws RSException {
		String entryURI = this.tableName + ":" + key;
		String url = Config.RS_HOST + "/get/" + encode(entryURI) + "/attName/"
				+ encode(attName) + "/base/" + base;
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			if (callRet.ok()) {
				return new GetRet(new JSONObject(callRet.getResult()));
			} else {
				return new GetRet(callRet);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RSException("Response is not in valid JSON format. ", e);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func Stat(key string) => (entry Entry, code int, err Error) ȡ��Դ����
	 */
	public StatRet stat(String key) throws RSException {
		String entryURI = this.tableName + ":" + key;
		String url = Config.RS_HOST + "/stat/" + encode(entryURI);
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			if (callRet.ok()) {
				return new StatRet(new JSONObject(callRet.getResult()));
			} else {
				return new StatRet(callRet);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RSException("Response is not in valid JSON format.", e);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func Publish(domain string) => (code int, err Error) ���� Table
	 * ��������Ϊ��̬��Դ��������̬��Դ��urlΪ��http://domain/key
	 */
	public PublishRet publish(String domain) throws RSException {
		String url = Config.RS_HOST + "/publish/" + encode(domain) + "/from/"
				+ this.tableName;
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			return new PublishRet(callRet);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func Unpublish(domain string) => (code int, err Error) ȡ������
	 */
	public PublishRet unpublish(String domain) throws RSException {
		String url = Config.RS_HOST + "/unpublish/" + encode(domain);
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			return new PublishRet(callRet);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func Delete(key string) => (code int, err Error) ɾ����Դ
	 */
	public DeleteRet delete(String key) throws RSException {
		String entryURI = this.tableName + ":" + key;
		String url = Config.RS_HOST + "/delete/" + encode(entryURI);
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			return new DeleteRet(callRet);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	/**
	 * func Drop() => (code int, err Error) ɾ�����������ã���
	 */
	public DropRet drop() throws RSException {
		String url = Config.RS_HOST + "/drop/" + this.tableName;
		CallRet callRet = null;
		try {
			callRet = conn.call(url, 1, 3);
			return new DropRet(callRet);
		} catch (AuthException e) {
			e.printStackTrace();
			throw new RSException("Auth failed: " + e.getMessage(), e);
		}
	}

	protected String encode(String text) {
		return this.encoder.encode(text.getBytes());
	}
}
