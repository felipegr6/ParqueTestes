package br.com.fgr.parquetestes.domain;

public interface OnCompletedOperation {
    void onSuccess(String message);

    void onError(String messageError);
}
