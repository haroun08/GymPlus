export interface IPeriod {
  id?: number;
  monthOccurrence?: number | null;
  dayOccurrence?: number | null;
}

export const defaultValue: Readonly<IPeriod> = {};
