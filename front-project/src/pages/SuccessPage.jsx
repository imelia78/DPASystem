import styled from 'styled-components';
import { useLocation, useNavigate } from 'react-router-dom';
import { CheckCircle2, Calendar, Clock, MapPin, Building2 } from 'lucide-react';
import { Button } from '../components/ui/Button';
import { useTranslation } from 'react-i18next';

const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 80vh;
`;

const SuccessCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 3rem;
  max-width: 600px;
  width: 100%;
  text-align: center;
  box-shadow: ${({ theme }) => theme.shadows.card};
`;

const IconWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 1.5rem;
  
  svg {
    color: ${({ theme }) => theme.colors.secondary};
    width: 80px;
    height: 80px;
  }
`;

const Title = styled.h1`
  font-size: 2.25rem;
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: 0.5rem;
`;

const Subtitle = styled.p`
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: 1.1rem;
  margin-bottom: 2.5rem;
`;

const DetailsBox = styled.div`
  background: ${({ theme }) => theme.colors.background};
  border-radius: ${({ theme }) => theme.radii.md};
  padding: 1.5rem;
  text-align: left;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2.5rem;
`;

const DetailRow = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  color: ${({ theme }) => theme.colors.text};
  font-size: 1.1rem;

  svg {
    color: ${({ theme }) => theme.colors.primary};
  }
`;

const SuccessPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { t } = useTranslation();
  const data = location.state || {};

  return (
    <Container>
      <SuccessCard>
        <IconWrapper>
          <CheckCircle2 />
        </IconWrapper>
        <Title>{t('success.title')}</Title>
        <Subtitle>{t('success.subtitle')}</Subtitle>

        <DetailsBox>
          <DetailRow>
            <Calendar size={24} /> <strong>{t('success.date')}</strong> {data.date || 'Oct 15, 2026'}
          </DetailRow>
          <DetailRow>
            <Clock size={24} /> <strong>{t('success.time')}</strong> {data.time || '10:00 AM'}
          </DetailRow>
          <DetailRow>
            <Building2 size={24} /> <strong>{t('success.clinic')}</strong> {data.clinicName || 'HealthBridge Center'}
          </DetailRow>
        </DetailsBox>

        <Button fullWidth onClick={() => navigate('/patient/dashboard')} style={{ padding: '1rem' }}>
          {t('success.goToDashboard')}
        </Button>
      </SuccessCard>
    </Container>
  );
};

export default SuccessPage;
