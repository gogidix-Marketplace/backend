export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

export function isValidEmailList(emails: string | string[]): boolean {
  const list = Array.isArray(emails) ? emails : [emails];
  return list.every(isValidEmail);
}
