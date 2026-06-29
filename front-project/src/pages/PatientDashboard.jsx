import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { Calendar, Clock, MapPin, User as UserIcon, Activity, CheckCircle, TrendingUp } from 'lucide-react';
import { Button } from '../components/ui/Button';
import { Card, CardTitle, CardText } from '../components/ui/Card';
import { appointmentService } from '../services/api';
import { useTranslation } from 'react-i18next';

const DashboardContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2.5rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
`;

const HeaderText = styled.div`
  h1 {
    font-size: 2rem;
    font-weight: 800;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.5rem;
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 1.1rem;
  }
`;



const ListContainer = styled(Card)`
  padding: 0;
  overflow: hidden;
`;

const ListHeader = styled.div`
  padding: 1.5rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};

  h2 {
    font-size: 1.25rem;
    font-weight: 700;
    color: ${({ theme }) => theme.colors.text};
  }

  button {
    background: transparent;
    border: none;
    color: ${({ theme }) => theme.colors.secondary};
    font-weight: 600;
    cursor: pointer;
    
    &:hover {
      text-decoration: underline;
    }
  }
`;

const ListItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: ${({ theme }) => theme.colors.surfaceHover};
  }
`;

const DoctorInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1.5rem;
`;

const Avatar = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: ${({ theme }) => `${theme.colors.secondary}15`};
  color: ${({ theme }) => theme.colors.secondary};
  display: flex;
  align-items: center;
  justify-content: center;
`;

const DoctorDetails = styled.div`
  h3 {
    font-size: 1.1rem;
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.25rem;
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.9rem;
  }
`;

const TimeInfo = styled.div`
  text-align: right;

  h4 {
    font-size: 1rem;
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.25rem;
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.9rem;
  }
`;

const CTABanner = styled.div`
  background: linear-gradient(135deg, ${({ theme }) => theme.colors.secondary} 0%, ${({ theme }) => theme.colors.primary} 100%);
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 3rem;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  @media (max-width: 768px) {
    flex-direction: column;
    text-align: center;
    gap: 2rem;
  }
`;

const CTAText = styled.div`
  h2 {
    font-size: 1.8rem;
    font-weight: 800;
    margin-bottom: 0.5rem;
  }
  p {
    font-size: 1.1rem;
    opacity: 0.9;
  }
`;

const TabsContainer = styled.div`
  display: flex;
  gap: 2rem;
  padding: 0 2rem;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
`;

const TabButton = styled.button`
  background: none;
  border: none;
  padding: 1rem 0;
  font-size: 1rem;
  font-weight: 600;
  color: ${({ $active, theme }) => $active ? theme.colors.primary : theme.colors.textMuted};
  border-bottom: 3px solid ${({ $active, theme }) => $active ? theme.colors.primary : 'transparent'};
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  margin-bottom: -1px;

  &:hover {
    color: ${({ theme, $active }) => $active ? theme.colors.primary : theme.colors.text};
  }
`;

const PatientDashboard = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [user, setUser] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [activeTab, setActiveTab] = useState('upcoming');

  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      const parsedUser = JSON.parse(stored);
      setUser(parsedUser);
      if (parsedUser.id) {
        appointmentService.getByClient(parsedUser.id).then(res => {
          setAppointments(res.data);
        }).catch(err => console.error("Failed to fetch appointments", err));
      }
    } else {
      navigate('/');
    }
  }, [navigate]);

  if (!user) return null;

  const upcoming = appointments.filter(a => a.appointmentStatus === 'ACTIVE' || a.appointmentStatus === 'PENDING' || !a.appointmentStatus);
  const completed = appointments.filter(a => a.appointmentStatus === 'COMPLETED');

  return (
    <DashboardContainer>
      
      <HeaderText>
        <h1>{t('patientDashboard.welcome')}, {user.firstName || 'Patient'}!</h1>
        <p>{t('patientDashboard.overview')}</p>
      </HeaderText>

      <ListContainer>
        <ListHeader style={{ borderBottom: 'none', paddingBottom: '0.5rem' }}>
          <h2>{t('patientDashboard.appointments', 'Appointments')}</h2>
          <button onClick={() => navigate('/patient/procedures')}>{t('patientDashboard.bookNewApp')}</button>
        </ListHeader>

        <TabsContainer>
          <TabButton 
            $active={activeTab === 'upcoming'} 
            onClick={() => setActiveTab('upcoming')}
          >
            {t('patientDashboard.upcomingApps')}
          </TabButton>
          <TabButton 
            $active={activeTab === 'completed'} 
            onClick={() => setActiveTab('completed')}
          >
            {t('patientDashboard.completedVisits')}
          </TabButton>
        </TabsContainer>
        
        {activeTab === 'upcoming' && (
          upcoming.length === 0 ? (
            <div style={{ padding: '3rem', textAlign: 'center', color: '#64748b' }}>
              {t('patientDashboard.noUpcomingApps')}
            </div>
          ) : (
            <div>
              {upcoming.map(app => (
                <ListItem key={app.id}>
                  <DoctorInfo>
                    <Avatar>
                      <UserIcon size={24} />
                    </Avatar>
                    <DoctorDetails>
                      <h3>{t('patientDashboard.dr')} {app.doctor?.firstName} {app.doctor?.lastName}</h3>
                      <p>{app.doctor?.specialization}</p>
                    </DoctorDetails>
                  </DoctorInfo>
                  <TimeInfo>
                    <h4>{app.appointmentDateTime ? format(new Date(app.appointmentDateTime), 'yyyy-MM-dd') : 'TBD'}</h4>
                    <p>{app.appointmentDateTime ? format(new Date(app.appointmentDateTime), 'hh:mm a') : 'TBD'}</p>
                  </TimeInfo>
                </ListItem>
              ))}
            </div>
          )
        )}

        {activeTab === 'completed' && (
          completed.length === 0 ? (
            <div style={{ padding: '3rem', textAlign: 'center', color: '#64748b' }}>
              {t('patientDashboard.noCompletedApps')}
            </div>
          ) : (
            <div>
              {completed.map(app => (
                <ListItem key={app.id}>
                  <DoctorInfo>
                    <Avatar>
                      <UserIcon size={24} />
                    </Avatar>
                    <DoctorDetails>
                      <h3>{t('patientDashboard.dr')} {app.doctor?.firstName} {app.doctor?.lastName}</h3>
                      <p>{app.doctor?.specialization}</p>
                    </DoctorDetails>
                  </DoctorInfo>
                  <TimeInfo>
                    <h4>{app.appointmentDateTime ? format(new Date(app.appointmentDateTime), 'yyyy-MM-dd') : 'TBD'}</h4>
                    <p>{app.appointmentDateTime ? format(new Date(app.appointmentDateTime), 'hh:mm a') : 'TBD'}</p>
                  </TimeInfo>
                </ListItem>
              ))}
            </div>
          )
        )}
      </ListContainer>

      <CTABanner>
        <CTAText>
          <h2>{t('patientDashboard.needAssistance')}</h2>
          <p>{t('patientDashboard.needAssistanceDesc')}</p>
        </CTAText>
        <Button 
          onClick={() => navigate('/patient/procedures')}
          style={{ 
            background: 'white', 
            color: '#3b82f6', 
            padding: '1rem 2rem' 
          }}
        >
          {t('patientDashboard.findDoctor')}
        </Button>
      </CTABanner>

    </DashboardContainer>
  );
};

export default PatientDashboard;
