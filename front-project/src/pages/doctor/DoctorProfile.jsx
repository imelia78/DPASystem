import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { User, Mail, Phone, Award, ShieldCheck, FileText, CheckCircle, Edit2, X, Save } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { doctorService } from '../../services/api';
import { useTranslation } from 'react-i18next';
import { extractAvatar, buildBioWithAvatar } from '../../utils/avatarUtils';
import { ImageCropperModal } from '../../components/ui/ImageCropperModal';
import { parseApiError } from '../../utils/errorHandler';
import { jwtDecode } from 'jwt-decode';

const PageContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const ProfileCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  overflow: hidden;
`;

const HeaderBanner = styled.div`
  background: linear-gradient(135deg, ${({ theme }) => theme.colors.primary}, ${({ theme }) => theme.colors.primaryDark});
  height: 120px;
  position: relative;
`;

const ProfileContent = styled.div`
  padding: 0 2rem 2rem;
  position: relative;
`;

const Avatar = styled.div`
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #fff;
  border: 4px solid #fff;
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: ${({ theme }) => theme.colors.primary};
  font-size: 2.5rem;
  margin-top: -50px;
  margin-bottom: 1rem;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  &:hover::after {
    content: 'Change';
    position: absolute;
    bottom: 0; left: 0; right: 0;
    background: rgba(0,0,0,0.6);
    color: white;
    font-size: 0.75rem;
    text-align: center;
    padding: 0.25rem 0;
  }
`;

const HiddenInput = styled.input`
  display: none;
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  
  @media (max-width: 600px) {
    grid-template-columns: 1fr;
  }
`;

const InfoGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

const Label = styled.label`
  font-size: 0.9rem;
  font-weight: 600;
  color: ${({ theme }) => theme.colors.textMuted};
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

const ValueBox = styled.div`
  padding: 0.75rem 1rem;
  background: ${({ theme }) => theme.colors.background};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  color: ${({ theme }) => theme.colors.text};
  font-weight: 500;
`;

const AdminCommentBox = styled.div`
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid ${({ theme }) => theme.colors.primary};
  border-radius: ${({ theme }) => theme.radii.md};
  padding: 1rem;
  margin-top: 2rem;
`;

const DoctorProfile = () => {
  const { t } = useTranslation();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [uploadingPhoto, setUploadingPhoto] = useState(false);
  const [cropperImageSrc, setCropperImageSrc] = useState(null);

  // Bio Edit State
  const [isEditingBio, setIsEditingBio] = useState(false);
  const [editBioText, setEditBioText] = useState('');
  const [savingBio, setSavingBio] = useState(false);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await doctorService.getMe();
        
        // Backend DTO is missing verificationStatus, so we infer it from the JWT
        let verificationStatus = 'PENDING';
        const token = localStorage.getItem('token');
        if (token) {
          const decoded = jwtDecode(token);
          const roles = decoded.realm_access?.roles || [];
          if (roles.includes('dpasystem.DOCTOR')) {
            verificationStatus = 'APPROVED';
          }
        }
        
        setUser({ ...response.data, verificationStatus });
      } catch (error) {
        console.error('Failed to fetch profile', error);
      } finally {
        setLoading(false);
      }
    };
    fetchUser();
  }, []);

  const handleFileSelect = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    
    // Clear the input so selecting the same file again triggers onChange
    e.target.value = null;

    const reader = new FileReader();
    reader.addEventListener('load', () => {
      setCropperImageSrc(reader.result);
    });
    reader.readAsDataURL(file);
  };

  const handleCroppedUpload = async (croppedBlob) => {
    setCropperImageSrc(null);
    setUploadingPhoto(true);
    try {
      const formData = new FormData();
      // append the cropped blob as 'image' with a filename
      formData.append('image', croppedBlob, 'avatar.jpg');
      
      const imgbbRes = await fetch('https://api.imgbb.com/1/upload?key=ee4c7b520f59c4adc7ea79c5bcd2e993', {
        method: 'POST',
        body: formData
      });
      const data = await imgbbRes.json();
      
      if (data.success) {
        const newPhotoUrl = data.data.url;
        const { cleanBio } = extractAvatar(user.professionalDescription);
        const newBio = buildBioWithAvatar(cleanBio, newPhotoUrl);
        
        const payload = { ...user, professionalDescription: newBio };
        await doctorService.update(payload);
        
        setUser(payload);
        localStorage.setItem('user', JSON.stringify({ role: 'doctor', ...payload }));
      } else {
        alert("Upload failed.");
      }
    } catch (err) {
      console.error(err);
      alert(parseApiError(err, "An error occurred during upload."));
    } finally {
      setUploadingPhoto(false);
    }
  };

  const handleSaveBio = async () => {
    if (editBioText.length > 200) {
      alert("Bio is too long!");
      return;
    }
    
    setSavingBio(true);
    try {
      const currentAvatarUrl = extractAvatar(user.professionalDescription).photoUrl;
      const newFullBio = buildBioWithAvatar(editBioText, currentAvatarUrl);
      
      const payload = { ...user, professionalDescription: newFullBio };
      await doctorService.update(payload);
      
      setUser(payload);
      localStorage.setItem('user', JSON.stringify({ role: 'doctor', ...payload }));
      setIsEditingBio(false);
    } catch (err) {
      console.error(err);
      alert(parseApiError(err, "Failed to update bio."));
    } finally {
      setSavingBio(false);
    }
  };

  if (loading) return <PageContainer style={{ padding: '3rem', textAlign: 'center' }}>Loading profile...</PageContainer>;
  if (!user) return null;

  return (
    <PageContainer>
      <ProfileCard>
        <HeaderBanner />
        <ProfileContent>
          <label>
            <Avatar style={{ opacity: uploadingPhoto ? 0.5 : 1 }}>
              {extractAvatar(user?.professionalDescription).photoUrl ? (
                <img src={extractAvatar(user.professionalDescription).photoUrl} alt="Avatar" />
              ) : (
                <User size={48} />
              )}
            </Avatar>
            <HiddenInput type="file" accept="image/*" onChange={handleFileSelect} disabled={uploadingPhoto} />
          </label>
          
          <h1 style={{ marginBottom: '0.25rem' }}>{t('patientDashboard.dr')} {user.firstName || 'First'} {user.lastName || 'Last'}</h1>
          <p style={{ color: '#64748b', marginBottom: '2rem' }}>{user.specialization || t('doctorProfileSelf.defaultSpecialization')}</p>

          <Grid>
            <InfoGroup>
              <Label><Mail size={16} /> {t('doctorProfileSelf.email')}</Label>
              <ValueBox>{user.email || 'doctor@example.com'}</ValueBox>
            </InfoGroup>
            
            <InfoGroup>
              <Label><Phone size={16} /> {t('doctorProfileSelf.phone')}</Label>
              <ValueBox>{user.phoneNumber || '+1 555-0199'}</ValueBox>
            </InfoGroup>
            
            <InfoGroup>
              <Label><Award size={16} /> {t('doctorProfileSelf.specialization')}</Label>
              <ValueBox>{user.specialization || t('doctorProfileSelf.defaultSpecialization')}</ValueBox>
            </InfoGroup>
            
            <InfoGroup>
              <Label><ShieldCheck size={16} /> {t('doctorProfileSelf.certificateNumber')}</Label>
              <ValueBox>{user.stateCertificateNumber || 'CERT-123456789'}</ValueBox>
            </InfoGroup>
            
            <InfoGroup style={{ gridColumn: '1 / -1' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Label><FileText size={16} /> {t('doctorProfileSelf.professionalDescription')}</Label>
                {!isEditingBio && (
                  <button 
                    onClick={() => {
                      setEditBioText(extractAvatar(user?.professionalDescription).cleanBio || '');
                      setIsEditingBio(true);
                    }}
                    style={{ background: 'none', border: 'none', color: '#3b82f6', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.25rem', fontSize: '0.85rem', fontWeight: '600' }}
                  >
                    <Edit2 size={14} /> Edit
                  </button>
                )}
              </div>
              
              <ValueBox style={{ minHeight: '100px' }}>
                {isEditingBio ? (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                    <textarea 
                      value={editBioText}
                      onChange={(e) => setEditBioText(e.target.value)}
                      maxLength={200}
                      rows={4}
                      style={{ 
                        width: '100%', 
                        padding: '0.5rem', 
                        borderRadius: '6px', 
                        border: '1px solid #cbd5e1',
                        fontFamily: 'inherit',
                        resize: 'vertical'
                      }}
                      placeholder="Write a brief bio (max 200 characters)..."
                    />
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <span style={{ fontSize: '0.8rem', color: editBioText.length >= 200 ? '#ef4444' : '#64748b' }}>
                        {editBioText.length} / 200 chars
                      </span>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <Button 
                          variant="outline" 
                          size="small" 
                          onClick={() => setIsEditingBio(false)}
                          disabled={savingBio}
                          style={{ padding: '0.25rem 0.5rem' }}
                        >
                          <X size={14} /> Cancel
                        </Button>
                        <Button 
                          size="small" 
                          onClick={handleSaveBio}
                          disabled={savingBio}
                          style={{ padding: '0.25rem 0.5rem' }}
                        >
                          <Save size={14} /> {savingBio ? 'Saving...' : 'Save'}
                        </Button>
                      </div>
                    </div>
                  </div>
                ) : (
                  extractAvatar(user?.professionalDescription).cleanBio || t('doctorProfileSelf.defaultDescription')
                )}
              </ValueBox>
            </InfoGroup>
          </Grid>

          {/* Verification Status */}
          {user.verificationStatus === 'APPROVED' ? (
            <AdminCommentBox>
              <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#047857', margin: 0 }}>
                <CheckCircle size={20} /> {t('doctorProfileSelf.verified')}
              </h3>
            </AdminCommentBox>
          ) : (
            <AdminCommentBox style={{ background: 'rgba(245, 158, 11, 0.1)', borderColor: '#f59e0b' }}>
              <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#b45309', margin: 0 }}>
                Pending Verification
              </h3>
              <p style={{ color: '#92400e', fontSize: '0.95rem', marginTop: '0.5rem' }}>
                Your profile is currently under administrative review.
              </p>
            </AdminCommentBox>
          )}
          
        </ProfileContent>
      </ProfileCard>

      {cropperImageSrc && (
        <ImageCropperModal 
          imageSrc={cropperImageSrc} 
          onCropComplete={handleCroppedUpload} 
          onCancel={() => setCropperImageSrc(null)} 
        />
      )}
    </PageContainer>
  );
};

export default DoctorProfile;
