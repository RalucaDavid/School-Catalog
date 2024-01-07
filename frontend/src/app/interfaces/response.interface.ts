export type Response<T> = {
    status: 'SUCCESS' | 'ERROR';
    data: T;
}