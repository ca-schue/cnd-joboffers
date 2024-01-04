

class ExternalApiError extends Error {

    public statusCode: number
    public errorCode: number
    public title: string
    public details: string | null


    constructor(statusCode: number, errorCode: number, title: string, details: string | null) {
        super();
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.title = title;
        this.details = details;
    }



}

export default ExternalApiError
