export const parseBackendDate = (dt) => {
  if (!dt) return null;
  if (Array.isArray(dt)) {
    // Spring Boot sometimes serializes LocalDateTime as an array: [year, month, day, hour, minute]
    const [y, m, d, h, min] = dt;
    return new Date(y, m - 1, d, h || 0, min || 0);
  }
  return new Date(dt);
};
