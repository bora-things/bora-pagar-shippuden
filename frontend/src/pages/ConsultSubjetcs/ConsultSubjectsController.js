export async function getSubjects() {
  const response = await fetch('/src/pages/ConsultSubjetcs/ConsultSubjectsMockData.json');
  return await response.json();
}