export interface SessionDto {
  id: number;
  gymId: number;
  workoutType: string;   // backend enum string (WorkoutType)
  startTime: string;     // ISO
  endTime: string;       // ISO
  capacity: number;
}

export interface SessionCreateDto {
  gymId: number;
  workoutType: string;
  startTime: string;
  endTime: string;
  capacity: number;
}
