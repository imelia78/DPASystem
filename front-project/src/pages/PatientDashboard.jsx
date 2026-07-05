import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { Calendar, Clock, MapPin, User as UserIcon, Activity, CheckCircle, TrendingUp, Star } from 'lucide-react';
import { Button } from '../components/ui/Button';
import { Card, CardTitle, CardText } from '../components/ui/Card';
import { Input } from '../components/ui/Input';
import { appointmentService, reviewService } from '../services/api';
import { useTranslation } from 'react-i18next';
import { extractAvatar } from '../utils/avatarUtils';
import { parseApiError } from '../utils/errorHandler';

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
  cursor: default;
  transition: ${({ theme }) => theme.transitions.default};

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: ${({ theme }) => theme.colors.surfaceHover};
  }

  @media (max-width: 768px) {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
`;

const DoctorInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex: 1;
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
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
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
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.5rem;

  h4 {
    font-size: 1rem;
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.9rem;
  }
  
  @media (max-width: 768px) {
    text-align: left;
    align-items: flex-start;
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

const ModalOverlay = styled.div`
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContent = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  padding: 2rem;
  border-radius: ${({ theme }) => theme.radii.lg};
  width: 90%;
  max-width: 450px;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  h3 {
    margin: 0;
    color: ${({ theme }) => theme.colors.text};
    font-size: 1.5rem;
  }
`;

const StarsContainer = styled.div`
  display: flex;
  gap: 0.5rem;
  justify-content: center;
  margin: 1rem 0;
`;

const StarButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  color: ${({ active }) => active ? '#eab308' : '#cbd5e1'};
  transition: transform 0.2s;
  
  &:hover {
    transform: scale(1.1);
  }
`;

const PatientDashboard = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [user, setUser] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [activeTab, setActiveTab] = useState('upcoming');
  const [loading, setLoading] = useState(false);

  // Review Modal State
  const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [submittingReview, setSubmittingReview] = useState(false);

  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      const parsedUser = JSON.parse(stored);
      setUser(parsedUser);
    } else {
      navigate('/');
    }
  }, [navigate]);

  const fetchAppointments = () => {
    if (user?.id) {
      setLoading(true);
      appointmentService.getByClient(user.id)
        .then(res => {
          const allApps = res.data || [];
          if (activeTab === 'upcoming') {
            const upcoming = allApps.filter(app => {
              const isPending = app.appointmentStatus === 'CREATED' || app.appointmentStatus === 'CONFIRMED';
              const isFuture = new Date(app.appointmentDateTime) >= new Date();
              return isPending && isFuture;
            });
            setAppointments(upcoming);
          } else {
            const completed = allApps.filter(app => {
              const isFinished = app.appointmentStatus === 'COMPLETED' || app.appointmentStatus === 'CANCELLED';
              const isPast = new Date(app.appointmentDateTime) < new Date();
              return isFinished || isPast;
            });
            setAppointments(completed);
          }
        })
        .catch(err => console.error("Failed to fetch appointments", err))
        .finally(() => setLoading(false));
    }
  };

  useEffect(() => {
    fetchAppointments();
    // eslint-disable-next-line
  }, [user, activeTab]);

  const openReviewModal = (app) => {
    setSelectedAppointment(app);
    setRating(5);
    setComment('');
    setIsReviewModalOpen(true);
  };

  const handleSubmitReview = async () => {
    if (!selectedAppointment) return;
    setSubmittingReview(true);
    
    try {
      const reviewPayload = {
        appointmentId: selectedAppointment.id,
        doctorId: selectedAppointment.doctor.id,
        clientId: user.id,
        rating: rating,
        comment: comment
      };
      
      await reviewService.create(reviewPayload);
      setIsReviewModalOpen(false);
      fetchAppointments(); // Refresh the list so the button hides
    } catch (err) {
      console.error("Failed to submit review", err);
      alert(parseApiError(err, "Failed to submit review."));
    } finally {
      setSubmittingReview(false);
    }
  };

  if (!user) return null;

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
        
        {loading ? (
          <div style={{ padding: '3rem', textAlign: 'center', color: '#64748b' }}>
            Loading...
          </div>
        ) : appointments.length === 0 ? (
          <div style={{ padding: '3rem', textAlign: 'center', color: '#64748b' }}>
            {activeTab === 'upcoming' ? t('patientDashboard.noUpcomingApps') : t('patientDashboard.noCompletedApps')}
          </div>
        ) : (
          <div>
            {appointments.map(app => (
              <ListItem key={app.id}>
                <DoctorInfo>
                  <Avatar>
                    {app.doctor && extractAvatar(app.doctor.professionalDescription).photoUrl ? (
                      <img src={extractAvatar(app.doctor.professionalDescription).photoUrl} alt="Doctor" />
                    ) : (
                      <UserIcon size={24} />
                    )}
                  </Avatar>
                  <DoctorDetails>
                    <h3>{t('patientDashboard.dr')} {app.doctor?.firstName} {app.doctor?.lastName}</h3>
                    <p>{app.doctor?.specialization}</p>
                  </DoctorDetails>
                </DoctorInfo>
                <TimeInfo>
                  <h4>{app.appointmentDateTime ? format(new Date(app.appointmentDateTime), 'yyyy-MM-dd') : 'TBD'}</h4>
                  <p>{app.appointmentDateTime ? format(new Date(app.appointmentDateTime), 'hh:mm a') : 'TBD'}</p>
                  
                  {/* Show Review Button or Status */}
                  {activeTab === 'completed' && app.appointmentStatus === 'COMPLETED' && (
                    <div style={{ marginTop: '0.5rem' }}>
                      {app.review ? (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.25rem', color: '#eab308', fontSize: '0.9rem', fontWeight: '600' }}>
                          <Star size={14} fill="#eab308" /> {app.review.rating} Rating
                        </div>
                      ) : (
                        <Button 
                          variant="outline" 
                          size="small" 
                          style={{ padding: '0.25rem 0.75rem', fontSize: '0.85rem' }}
                          onClick={() => openReviewModal(app)}
                        >
                          Leave Review
                        </Button>
                      )}
                    </div>
                  )}
                </TimeInfo>
              </ListItem>
            ))}
          </div>
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

      {/* Review Modal */}
      {isReviewModalOpen && (
        <ModalOverlay onClick={() => !submittingReview && setIsReviewModalOpen(false)}>
          <ModalContent onClick={e => e.stopPropagation()}>
            <h3>Rate your visit</h3>
            <p style={{ color: '#64748b', fontSize: '0.95rem' }}>
              How was your appointment with Dr. {selectedAppointment?.doctor?.lastName}?
            </p>
            
            <StarsContainer>
              {[1, 2, 3, 4, 5].map((star) => (
                <StarButton 
                  key={star} 
                  active={star <= rating}
                  onClick={() => setRating(star)}
                >
                  <Star size={36} fill={star <= rating ? "#eab308" : "none"} />
                </StarButton>
              ))}
            </StarsContainer>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
              <label style={{ fontSize: '0.9rem', color: '#64748b', fontWeight: '500' }}>Add a comment (optional)</label>
              <textarea 
                rows={4}
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                style={{ 
                  width: '100%', 
                  padding: '0.75rem', 
                  borderRadius: '8px', 
                  border: '1px solid #e2e8f0',
                  fontFamily: 'inherit',
                  resize: 'vertical'
                }}
                placeholder="Share your experience..."
              />
            </div>

            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
              <Button variant="secondary" onClick={() => setIsReviewModalOpen(false)} disabled={submittingReview}>
                Cancel
              </Button>
              <Button onClick={handleSubmitReview} disabled={submittingReview}>
                {submittingReview ? 'Submitting...' : 'Submit Review'}
              </Button>
            </div>
          </ModalContent>
        </ModalOverlay>
      )}

    </DashboardContainer>
  );
};

export default PatientDashboard;
