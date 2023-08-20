declare module "vocabularyTypes" {
  export interface Vocabulary {
    id: number;
    title: string;
    language: string;
    unitCount: number;
  }

  export interface VocaSearchCondition {
    language?: string;
  }

  export interface VocaRegistrationFormData {
    title: string;
    language: string;
  }

  export interface VocaModificationFormData {
    title: string;
    language: string;
  }
}
