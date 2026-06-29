import { useState } from 'react';
import styled from 'styled-components';
import { Calendar as CalendarIcon, Users, Star, TrendingUp, Clock, User as UserIcon } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { useTranslation } from 'react-i18next';

const DashboardContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
`;

const Header = styled.div`
  margin-bottom: 0.5rem;
  
  h1 {
    font-size: 1.8rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.25rem;
  }
  
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.95rem;
  }
`;

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  
  @media (max-width: 1024px) {
    grid-template-columns: repeat(2, 1fr);
  }
  @media (max-width: 600px) {
    grid-template-columns: 1fr;
  }
`;

const StatCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  
  .icon-wrapper {
    width: 48px;
    height: 48px;
    border-radius: ${({ theme }) => theme.radii.md};
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .content {
    display: flex;
    flex-direction: column;
  }
  
  .label {
    font-size: 0.85rem;
    color: ${({ theme }) => theme.colors.textMuted};
    font-weight: 500;
  }
  
  .value {
    font-size: 1.5rem;
    font-weight: 700;
    color: ${({ theme }) => theme.colors.text};
  }
`;

const MainContentGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  
  @media (max-width: 900px) {
    grid-template-columns: 1fr;
  }
`;

const SectionContainer = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  min-height: 400px;
`;

const SectionTitle = styled.h3`
  font-size: 1.1rem;
  color: ${({ theme }) => theme.colors.text};
  font-weight: 600;
  margin-bottom: 0.5rem;
`;

const AppointmentRow = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  
  .left {
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  
  .avatar {
    width: 40px;
    height: 40px;
    background: ${({ theme }) => theme.colors.surfaceHover};
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: ${({ theme }) => theme.colors.textMuted};
  }
  
  .details {
    display: flex;
    flex-direction: column;
    
    .name {
      font-weight: 600;
      color: ${({ theme }) => theme.colors.text};
      font-size: 0.95rem;
    }
    
    .type {
      font-size: 0.8rem;
      color: ${({ theme }) => theme.colors.textMuted};
    }
  }
  
  .right {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 0.25rem;
    
    .time {
      font-size: 0.85rem;
      font-weight: 600;
      color: ${({ theme }) => theme.colors.text};
      display: flex;
      align-items: center;
      gap: 0.25rem;
    }
  }
`;

const StatusPill = styled.span`
  padding: 0.2rem 0.6rem;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 600;
  text-transform: lowercase;
  
  ${({ status, theme }) => {
    if (status === 'confirmed') return `background: #dcfce7; color: #166534;`;
    if (status === 'pending') return `background: #fef9c3; color: #854d0e;`;
    return `background: ${theme.colors.surfaceHover}; color: ${theme.colors.textMuted};`;
  }}
`;

const ReviewRow = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 1rem;
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .name {
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
    font-size: 0.95rem;
  }
  
  .stars {
    display: flex;
    gap: 0.1rem;
    color: #f59e0b;
  }
  
  .comment {
    font-size: 0.85rem;
    color: ${({ theme }) => theme.colors.textMuted};
    line-height: 1.4;
  }
  
  .date {
    font-size: 0.75rem;
    color: ${({ theme }) => theme.colors.textMuted};
    opacity: 0.7;
    margin-top: 0.25rem;
  }
`;

const Banner = styled.div`
  background: linear-gradient(135deg, ${({ theme }) => theme.colors.primary}, ${({ theme }) => theme.colors.secondary});
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 2rem 2.5rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1rem;
  color: white;
  margin-top: 1rem;
  
  h2 {
    font-size: 1.5rem;
    font-weight: 700;
    margin: 0;
  }
  
  p {
    font-size: 0.95rem;
    opacity: 0.9;
    margin: 0 0 0.5rem;
  }
`;

const WhiteButton = styled.button`
  background: white;
  color: ${({ theme }) => theme.colors.secondary};
  font-weight: 600;
  padding: 0.75rem 1.5rem;
  border-radius: ${({ theme }) => theme.radii.md};
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 0.95rem;
  
  &:hover {
    background: #f8fafc;
    transform: translateY(-1px);
    box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);
  }
`;

const DoctorDashboard = () => {
  const { t } = useTranslation();
  const userStr = localStorage.getItem('user');
  const user = userStr ? JSON.parse(userStr) : {};
  const lastName = user.lastName || 'Doctor';

  return (
    <DashboardContainer>
      <Header>
        <h1>{t('doctorDashboard.welcome')} {lastName}!</h1>
        <p>{t('doctorDashboard.subtitle')}</p>
      </Header>

      <StatsGrid>
        <StatCard>
          <div className="icon-wrapper" style={{ background: '#eff6ff', color: '#3b82f6' }}>
            <CalendarIcon size={24} />
          </div>
          <div className="content">
            <span className="label">{t('doctorDashboard.todaysAppointments')}</span>
            <span className="value">4</span>
          </div>
        </StatCard>
        
        <StatCard>
          <div className="icon-wrapper" style={{ background: '#dcfce7', color: '#22c55e' }}>
            <Users size={24} />
          </div>
          <div className="content">
            <span className="label">{t('doctorDashboard.totalPatients')}</span>
            <span className="value">247</span>
          </div>
        </StatCard>
        
        <StatCard>
          <div className="icon-wrapper" style={{ background: '#fef3c7', color: '#f59e0b' }}>
            <Star size={24} />
          </div>
          <div className="content">
            <span className="label">{t('doctorDashboard.averageRating')}</span>
            <span className="value">4.8</span>
          </div>
        </StatCard>
        
        <StatCard>
          <div className="icon-wrapper" style={{ background: '#f3e8ff', color: '#a855f7' }}>
            <TrendingUp size={24} />
          </div>
          <div className="content">
            <span className="label">{t('doctorDashboard.thisWeek')}</span>
            <span className="value">32</span>
          </div>
        </StatCard>
      </StatsGrid>

      <MainContentGrid>
        <SectionContainer>
          <SectionTitle>{t('doctorDashboard.todaysSchedule')}</SectionTitle>
          
          <AppointmentRow>
            <div className="left">
              <div className="avatar"><UserIcon size={20} /></div>
              <div className="details">
                <span className="name">John Smith</span>
                <span className="type">{t('doctorDashboard.followUp')}</span>
              </div>
            </div>
            <div className="right">
              <span className="time"><Clock size={14} /> 10:00 AM</span>
              <StatusPill status="confirmed">{t('doctorDashboard.confirmed')}</StatusPill>
            </div>
          </AppointmentRow>
          
          <AppointmentRow>
            <div className="left">
              <div className="avatar"><UserIcon size={20} /></div>
              <div className="details">
                <span className="name">Sarah Williams</span>
                <span className="type">{t('doctorDashboard.initialConsultation')}</span>
              </div>
            </div>
            <div className="right">
              <span className="time"><Clock size={14} /> 11:30 AM</span>
              <StatusPill status="confirmed">{t('doctorDashboard.confirmed')}</StatusPill>
            </div>
          </AppointmentRow>
          
          <AppointmentRow>
            <div className="left">
              <div className="avatar"><UserIcon size={20} /></div>
              <div className="details">
                <span className="name">Michael Brown</span>
                <span className="type">{t('doctorDashboard.checkUp')}</span>
              </div>
            </div>
            <div className="right">
              <span className="time"><Clock size={14} /> 2:00 PM</span>
              <StatusPill status="confirmed">{t('doctorDashboard.confirmed')}</StatusPill>
            </div>
          </AppointmentRow>
          
          <AppointmentRow>
            <div className="left">
              <div className="avatar"><UserIcon size={20} /></div>
              <div className="details">
                <span className="name">Emily Davis</span>
                <span className="type">{t('doctorDashboard.initialConsultation')}</span>
              </div>
            </div>
            <div className="right">
              <span className="time"><Clock size={14} /> 3:30 PM</span>
              <StatusPill status="pending">{t('doctorDashboard.pending')}</StatusPill>
            </div>
          </AppointmentRow>

        </SectionContainer>

        <SectionContainer>
          <SectionTitle>{t('doctorDashboard.recentReviews')}</SectionTitle>
          
          <ReviewRow>
            <div className="header">
              <span className="name">Robert Johnson</span>
              <div className="stars">
                {[...Array(5)].map((_, i) => <Star key={i} size={14} fill="currentColor" />)}
              </div>
            </div>
            <span className="comment">Excellent doctor! Very thorough and caring.</span>
            <span className="date">2026-05-14</span>
          </ReviewRow>
          
          <ReviewRow>
            <div className="header">
              <span className="name">Lisa Martinez</span>
              <div className="stars">
                {[...Array(5)].map((_, i) => <Star key={i} size={14} fill="currentColor" />)}
              </div>
            </div>
            <span className="comment">Great experience, highly recommend!</span>
            <span className="date">2026-05-12</span>
          </ReviewRow>
          
          <ReviewRow>
            <div className="header">
              <span className="name">David Wilson</span>
              <div className="stars">
                {[...Array(4)].map((_, i) => <Star key={i} size={14} fill="currentColor" />)}
                <Star size={14} color="#cbd5e1" />
              </div>
            </div>
            <span className="comment">Professional and knowledgeable.</span>
            <span className="date">2026-05-10</span>
          </ReviewRow>

        </SectionContainer>
      </MainContentGrid>

      <Banner>
        <h2>{t('doctorDashboard.manageSchedule')}</h2>
        <p>{t('doctorDashboard.manageSubtitle')}</p>
        <WhiteButton>{t('doctorDashboard.manageAvailability')}</WhiteButton>
      </Banner>

    </DashboardContainer>
  );
};

export default DoctorDashboard;
