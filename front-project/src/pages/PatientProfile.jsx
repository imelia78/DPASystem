import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { clientService } from '../services/api';
import { Button } from '../components/ui/Button';
import { Input, Label, FormGroup, Select } from '../components/ui/Input';
import { User, Mail, Calendar, Phone, HeartPulse } from 'lucide-react';
import { useTranslation } from 'react-i18next';

const PageContainer = styled.div`
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const Header = styled.div`
  h1 {
    font-size: 2rem;
    color: ${({ theme }) => theme.colors.text};
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

const ProfileCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 2.5rem;
  box-shadow: ${({ theme }) => theme.shadows.card};
`;

const AvatarContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 2rem;
  
  .avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background: ${({ theme }) => theme.colors.primary};
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 1rem;
    box-shadow: ${({ theme }) => theme.shadows.glow};
  }
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const Row = styled.div`
  display: flex;
  gap: 1rem;
  width: 100%;
`;

const PatientProfile = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [originalUser, setOriginalUser] = useState(null);
  const [user, setUser] = useState({
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    dateOfBirth: '',
    sex: 'MALE',
    password: ''
  });

  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      const parsed = JSON.parse(stored);
      setUser(parsed);
      setOriginalUser(parsed);
    } else {
      navigate('/');
    }
  }, [navigate]);

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      let updatedData = { ...user };

      // 1. Update Email via PATCH if changed
      if (originalUser && user.email !== originalUser.email) {
        const emailRes = await clientService.updateEmail(user.id, user.email);
        updatedData = { ...updatedData, ...emailRes.data };
      }

      // 2. Update Phone via PATCH if changed
      if (originalUser && user.phoneNumber !== originalUser.phoneNumber) {
        const phoneRes = await clientService.updatePhone(user.id, user.phoneNumber);
        updatedData = { ...updatedData, ...phoneRes.data };
      }

      // 3. Update remaining fields via PUT if they changed
      const basicFieldsChanged = !originalUser || 
        user.firstName !== originalUser.firstName ||
        user.lastName !== originalUser.lastName ||
        user.dateOfBirth !== originalUser.dateOfBirth;

      if (basicFieldsChanged) {
        const { role, ...clientDtoPayload } = user;
        const profileRes = await clientService.updateProfile(clientDtoPayload);
        updatedData = { ...updatedData, ...profileRes.data };
      }

      const finalUser = { role: 'patient', ...updatedData };
      localStorage.setItem('user', JSON.stringify(finalUser));
      setUser(finalUser);
      setOriginalUser(finalUser);
      
      alert(t('profile.updateSuccess'));
    } catch (err) {
      console.error("Failed to update profile", err);
      alert(t('profile.updateError'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageContainer>
      <Header>
        <h1>{t('profile.title')}</h1>
        <p>{t('profile.subtitle')}</p>
      </Header>

      <ProfileCard>
        <AvatarContainer>
          <div className="avatar">
            {user.firstName ? user.firstName[0] : ''}
            {user.lastName ? user.lastName[0] : ''}
          </div>
          <h2 style={{ fontSize: '1.25rem' }}>{user.firstName} {user.lastName}</h2>
        </AvatarContainer>

        <Form onSubmit={handleSave}>
          <Row>
            <FormGroup>
              <Label><User size={14} style={{ marginRight: '4px' }} /> {t('auth.firstName')}</Label>
              <Input name="firstName" value={user.firstName} onChange={handleChange} />
            </FormGroup>
            <FormGroup>
              <Label><User size={14} style={{ marginRight: '4px' }} /> {t('auth.lastName')}</Label>
              <Input name="lastName" value={user.lastName} onChange={handleChange} />
            </FormGroup>
          </Row>

          <FormGroup>
            <Label><Mail size={14} style={{ marginRight: '4px' }} /> {t('auth.email')}</Label>
            <Input type="email" name="email" value={user.email} onChange={handleChange} />
          </FormGroup>

          <Row>
            <FormGroup>
              <Label><Calendar size={14} style={{ marginRight: '4px' }} /> {t('auth.dob')}</Label>
              <Input type="date" name="dateOfBirth" value={user.dateOfBirth} onChange={handleChange} />
            </FormGroup>
            <FormGroup>
              <Label><Phone size={14} style={{ marginRight: '4px' }} /> {t('auth.phone')}</Label>
              <Input type="tel" name="phoneNumber" value={user.phoneNumber || ''} onChange={handleChange} />
            </FormGroup>
          </Row>

          <Button type="submit" disabled={loading} style={{ marginTop: '1rem', padding: '1rem' }}>
            {loading ? t('profile.saving') : t('profile.save')}
          </Button>
        </Form>
      </ProfileCard>
    </PageContainer>
  );
};

export default PatientProfile;
