export interface BookingDto {
  id: number;
  userId: number;
  sessionId: number;
  status: 'CONFIRMED' | 'CANCELLED';
}

export interface BookingCreateDto {
  sessionId: number;
}
