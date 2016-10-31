package br.fgr.domain;

public interface OnCompletedOperation {
	void onSuccess(String message);

	void onError(String messageError);
}
