
interface Teacher {
  id: number;
  fullName: string;
}

export interface Subject {
  id: number;
  name: string;
  description: string;
  teachers?: Array<Teacher>;
}