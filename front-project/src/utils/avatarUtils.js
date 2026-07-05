export const extractAvatar = (bioString) => {
  if (!bioString) return { photoUrl: null, cleanBio: '' };
  
  const match = bioString.match(/\[PHOTO:(.*?)\]/);
  if (match && match[1]) {
    const photoUrl = match[1];
    const cleanBio = bioString.replace(/\[PHOTO:.*?\]\s*/, '').trim();
    return { photoUrl, cleanBio };
  }
  
  return { photoUrl: null, cleanBio: bioString };
};

export const buildBioWithAvatar = (cleanBio, photoUrl) => {
  if (!photoUrl) return cleanBio;
  return `[PHOTO:${photoUrl}] ${cleanBio}`;
};
