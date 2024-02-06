export interface User {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    superUser?: boolean;

    learningSubjects: Array<any>;
    teachingSubjects: Array<any>;
};