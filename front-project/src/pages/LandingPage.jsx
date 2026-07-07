import React from 'react';
import styled, { keyframes } from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { HeartPulse, Users, CalendarCheck, Star, ShieldCheck, Stethoscope, Clock, CircleDollarSign, MessageSquare } from 'lucide-react';
import { Button } from '../components/ui/Button';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../components/ui/LanguageSwitcher';

const fadeIn = keyframes`
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
`;

const float = keyframes`
  0% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(0px); }
`;

const PageContainer = styled.div`
  min-height: 100vh;
  background: ${({ theme }) => theme.colors.background};
  font-family: 'Inter', sans-serif;
  overflow-x: hidden;
`;

const Navbar = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 5%;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  position: sticky;
  top: 0;
  z-index: 100;
  border-bottom: 1px solid rgba(0,0,0,0.05);
`;

const Logo = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.5rem;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};

  svg {
    color: ${({ theme }) => theme.colors.primary};
  }
`;

const NavActions = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
`;

const HeroSection = styled.section`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6rem 5%;
  min-height: 80vh;
  background: linear-gradient(135deg, rgba(59,130,246,0.05) 0%, rgba(16,185,129,0.05) 100%);

  @media (max-width: 968px) {
    flex-direction: column;
    text-align: center;
    gap: 4rem;
  }
`;

const HeroText = styled.div`
  flex: 1;
  max-width: 600px;
  animation: ${fadeIn} 0.6s ease-out;

  h1 {
    font-size: 4rem;
    font-weight: 800;
    color: ${({ theme }) => theme.colors.text};
    line-height: 1.2;
    margin-bottom: 1.5rem;
    
    span {
      color: ${({ theme }) => theme.colors.primary};
    }
  }

  p {
    font-size: 1.2rem;
    color: ${({ theme }) => theme.colors.textMuted};
    line-height: 1.6;
    margin-bottom: 2.5rem;
  }
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 1rem;
  
  @media (max-width: 968px) {
    justify-content: center;
  }
`;

const HeroVisual = styled.div`
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  position: relative;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const StatCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  padding: 2rem;
  border-radius: ${({ theme }) => theme.radii.lg};
  box-shadow: ${({ theme }) => theme.shadows.card};
  display: flex;
  flex-direction: column;
  gap: 1rem;
  animation: ${float} 6s ease-in-out infinite;
  animation-delay: ${({ $delay }) => $delay || '0s'};
  border: 1px solid rgba(255,255,255,0.8);
  backdrop-filter: blur(10px);
  
  h3 {
    font-size: 1.5rem;
    font-weight: 700;
    color: ${({ theme }) => theme.colors.text};
  }
  
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.9rem;
  }
`;

const IconWrapper = styled.div`
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: ${({ $color }) => `${$color}15`};
  color: ${({ $color }) => $color};
`;

const Section = styled.section`
  padding: 6rem 5%;
  text-align: center;
`;

const SectionHeader = styled.div`
  margin-bottom: 4rem;
  
  h2 {
    font-size: 2.5rem;
    font-weight: 700;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 1rem;
  }
  
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 1.1rem;
  }
`;

const StepsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 3rem;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const Step = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
`;

const StepNumber = styled.div`
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
  background: ${({ $color, theme }) => `${$color || theme.colors.primary}15`};
  color: ${({ $color, theme }) => $color || theme.colors.primary};
`;

const FeaturesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  
  @media (max-width: 1024px) {
    grid-template-columns: repeat(2, 1fr);
  }
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const FeatureCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  padding: 2.5rem 1.5rem;
  border-radius: ${({ theme }) => theme.radii.lg};
  box-shadow: ${({ theme }) => theme.shadows.card};
  transition: ${({ theme }) => theme.transitions.default};
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 1.5rem;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
  }
  
  h4 {
    font-size: 1.25rem;
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
  }
  
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.95rem;
    line-height: 1.5;
  }
`;

const CTASection = styled.section`
  padding: 6rem 5%;
  background: linear-gradient(135deg, ${({ theme }) => theme.colors.secondary} 0%, ${({ theme }) => theme.colors.primary} 100%);
  text-align: center;
  color: white;
  
  h2 {
    font-size: 3rem;
    font-weight: 800;
    margin-bottom: 1rem;
  }
  
  p {
    font-size: 1.2rem;
    opacity: 0.9;
    margin-bottom: 2.5rem;
  }
`;

const LandingPage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const userStr = localStorage.getItem('user');
  const user = userStr ? JSON.parse(userStr) : null;

  const handleGetStarted = () => {
    if (user) {
      navigate(user.role === 'doctor' ? '/doctor/dashboard' : user.role === 'admin' ? '/admin/dashboard' : '/patient/dashboard');
    } else {
      navigate('/auth?mode=register');
    }
  };

  return (
    <PageContainer>
      <Navbar>
        <Logo>
          <HeartPulse size={32} />
          HealthBridge
        </Logo>
        <NavActions>
          <LanguageSwitcher />
          {!user && <Button variant="ghost" onClick={() => navigate('/auth?mode=login')}>{t('landing.signIn')}</Button>}
          <Button onClick={handleGetStarted}>{user ? 'Dashboard' : t('nav.getStarted')}</Button>
        </NavActions>
      </Navbar>

      <HeroSection>
        <HeroText>
          <h1>{t('landing.heroTitle1')}<br/>{t('landing.heroTitle2')} <span>{t('landing.heroTitleHighlight')}</span></h1>
          <p>{t('landing.heroSubtitle')}</p>
          <ButtonGroup>
            <Button onClick={handleGetStarted} style={{ padding: '1rem 2rem' }}>
              {user ? 'Go to Dashboard' : t('landing.bookAppointment')} &rarr;
            </Button>
            {!user && (
              <Button variant="secondary" onClick={() => navigate('/auth?mode=login')} style={{ padding: '1rem 2rem' }}>
                {t('landing.signIn')}
              </Button>
            )}
          </ButtonGroup>
        </HeroText>

        <HeroVisual>
          <StatCard $delay="0s">
            <IconWrapper $color="#3b82f6"><Users size={24} /></IconWrapper>
            <h3>{t('landing.stats.network')}</h3>
            <p>{t('landing.stats.networkDesc')}</p>
          </StatCard>
          <StatCard $delay="1.5s" style={{ transform: 'translateY(20px)' }}>
            <IconWrapper $color="#10b981"><CalendarCheck size={24} /></IconWrapper>
            <h3>{t('landing.stats.booking')}</h3>
            <p>{t('landing.stats.bookingDesc')}</p>
          </StatCard>
          <StatCard $delay="0.75s">
            <IconWrapper $color="#f59e0b"><Star size={24} /></IconWrapper>
            <h3>{t('landing.stats.rated')}</h3>
            <p>{t('landing.stats.ratedDesc')}</p>
          </StatCard>
          <StatCard $delay="2.25s" style={{ transform: 'translateY(20px)' }}>
            <IconWrapper $color="#8b5cf6"><ShieldCheck size={24} /></IconWrapper>
            <h3>{t('landing.stats.trusted')}</h3>
            <p>{t('landing.stats.trustedDesc')}</p>
          </StatCard>
        </HeroVisual>
      </HeroSection>

      <Section>
        <SectionHeader>
          <h2>{t('landing.howItWorks.title')}</h2>
          <p>{t('landing.howItWorks.subtitle')}</p>
        </SectionHeader>
        <StepsGrid>
          <Step>
            <StepNumber $color="#3b82f6">1</StepNumber>
            <h3>{t('landing.howItWorks.step1')}</h3>
            <p style={{ color: '#64748b' }}>{t('landing.howItWorks.step1Desc')}</p>
          </Step>
          <Step>
            <StepNumber $color="#10b981">2</StepNumber>
            <h3>{t('landing.howItWorks.step2')}</h3>
            <p style={{ color: '#64748b' }}>{t('landing.howItWorks.step2Desc')}</p>
          </Step>
          <Step>
            <StepNumber $color="#8b5cf6">3</StepNumber>
            <h3>{t('landing.howItWorks.step3')}</h3>
            <p style={{ color: '#64748b' }}>{t('landing.howItWorks.step3Desc')}</p>
          </Step>
        </StepsGrid>
      </Section>

      <Section style={{ background: 'rgba(16,185,129,0.03)' }}>
        <SectionHeader>
          <h2>{t('landing.whyChoose.title')}</h2>
          <p>{t('landing.whyChoose.subtitle')}</p>
        </SectionHeader>
        <FeaturesGrid>
          <FeatureCard>
            <IconWrapper $color="#10b981" style={{ width: 64, height: 64 }}>
              <Users size={32} />
            </IconWrapper>
            <h4>{t('landing.whyChoose.expert')}</h4>
            <p>{t('landing.whyChoose.expertDesc')}</p>
          </FeatureCard>
          <FeatureCard>
            <IconWrapper $color="#3b82f6" style={{ width: 64, height: 64 }}>
              <Clock size={32} />
            </IconWrapper>
            <h4>{t('landing.whyChoose.easy')}</h4>
            <p>{t('landing.whyChoose.easyDesc')}</p>
          </FeatureCard>
          <FeatureCard>
            <IconWrapper $color="#f59e0b" style={{ width: 64, height: 64 }}>
              <CircleDollarSign size={32} />
            </IconWrapper>
            <h4>{t('landing.whyChoose.transparent')}</h4>
            <p>{t('landing.whyChoose.transparentDesc')}</p>
          </FeatureCard>
          <FeatureCard>
            <IconWrapper $color="#8b5cf6" style={{ width: 64, height: 64 }}>
              <MessageSquare size={32} />
            </IconWrapper>
            <h4>{t('landing.whyChoose.reviews')}</h4>
            <p>{t('landing.whyChoose.reviewsDesc')}</p>
          </FeatureCard>
        </FeaturesGrid>
      </Section>

      <CTASection>
        <h2>{t('landing.cta.title')}</h2>
        <p>{t('landing.cta.subtitle')}</p>
        <Button 
          onClick={() => navigate('/auth?mode=register')} 
          style={{ 
            background: 'white', 
            color: '#10b981', 
            padding: '1.2rem 2.5rem', 
            fontSize: '1.1rem',
            boxShadow: '0 10px 25px rgba(0,0,0,0.1)'
          }}
        >
          {t('landing.cta.button')} &rarr;
        </Button>
      </CTASection>
    </PageContainer>
  );
};

export default LandingPage;
