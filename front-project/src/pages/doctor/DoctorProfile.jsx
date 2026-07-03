import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { User, Mail, Phone, Award, ShieldCheck, FileText, CheckCircle } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { doctorService } from '../../services/api';
import { useTranslation } from 'react-i18next';

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

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await doctorService.getMe();
        setUser(response.data);
      } catch (error) {
        console.error('Failed to fetch profile', error);
      } finally {
        setLoading(false);
      }
    };
    fetchUser();
  }, []);

  if (loading) return <PageContainer style={{ padding: '3rem', textAlign: 'center' }}>Loading profile...</PageContainer>;
  if (!user) return null;

  return (
    <PageContainer>
      <ProfileCard>
        <HeaderBanner />
        <ProfileContent>
          <Avatar>
            <User size={48} />
          </Avatar>
          
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
              <Label><FileText size={16} /> {t('doctorProfileSelf.professionalDescription')}</Label>
              <ValueBox style={{ minHeight: '100px' }}>
                {user.professionalDescription || t('doctorProfileSelf.defaultDescription')}
              </ValueBox>
            </InfoGroup>
          </Grid>

          {/* Verification Status */}
          <AdminCommentBox>
            <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#047857', marginBottom: '0.5rem' }}>
              <CheckCircle size={20} /> {t('doctorProfileSelf.verified')}
            </h3>
            <p style={{ color: '#065f46', fontSize: '0.95rem' }}>
              {t('doctorProfileSelf.adminComment')} <i>{t('doctorProfileSelf.adminCommentDefault')}</i>
            </p>
          </AdminCommentBox>
          
        </ProfileContent>
      </ProfileCard>
    </PageContainer>
  );
};

export default DoctorProfile;
