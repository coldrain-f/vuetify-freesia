export type Vocabulary = {
  id: number;
  title: string;
  language: string;
  unitCount: number;
};

export type VocaSearchCondition = {
  language?: string;
};

export type VocaRegistrationFormData = {
  title: string;
  language: string;
};

export type VocaModificationFormData = {
  title: string;
  language: string;
};
