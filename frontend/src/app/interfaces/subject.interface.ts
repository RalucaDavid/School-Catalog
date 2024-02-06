
export interface Teacher {
  id: number;
  fullName: string;
}

export interface Subject {
  id: number;
  name: string;
  description?: string;
  teachers?: Array<Teacher>;
}

export interface Grade {
  grade: number;
  timestamp: number;
  final: boolean;
};

export type LearningSubjectAndGrades = [string, number, number, boolean];

export type SubjectStudents = Array<{
  userLearningSubjectId: number;
  userName: string;
  grades: Array<Grade>;
}>;