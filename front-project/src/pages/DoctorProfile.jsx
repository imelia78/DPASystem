import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useParams, useNavigate } from 'react-router-dom';
import { doctorService, reviewService } from '../services/api';
import { Button } from '../components/ui/Button';
import { Star, CheckCircle2, ChevronLeft, Calendar } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import { extractAvatar } from '../utils/avatarUtils';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 1000px;
  margin: 0 auto;
`;

const BackButton = styled.button`
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: ${({ theme }) => theme.colors.surface};
  color: ${({ theme }) => theme.colors.textMuted};
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: ${({ theme }) => theme.radii.full};
  border: 1px solid ${({ theme }) => theme.colors.border};
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  align-self: flex-start;
  
  &:hover {
    color: ${({ theme }) => theme.colors.primary};
    border-color: ${({ theme }) => theme.colors.primary};
    background: ${({ theme }) => theme.colors.surfaceHover};
    transform: translateY(-1px);
  }
`;

const ProfileCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 2.5rem;
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 2.5rem;
  box-shadow: ${({ theme }) => theme.shadows.card};

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const Avatar = styled.div`
  width: 150px;
  height: 150px;
  border-radius: ${({ theme }) => theme.radii.lg};
  background: ${({ theme }) => theme.colors.surfaceHover};
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3.5rem;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.primary};
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ProfileInfo = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;

  h1 {
    font-size: 2.5rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.5rem;
  }
  
  .specialty {
    color: ${({ theme }) => theme.colors.primary};
    font-size: 1.25rem;
    font-weight: 500;
    margin-bottom: 1rem;
  }
`;

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
  margin: 1.5rem 0;
  padding: 1.5rem 0;
  border-top: 1px solid ${({ theme }) => theme.colors.border};
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};

  div {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
    
    span:first-child {
      color: ${({ theme }) => theme.colors.textMuted};
      font-size: 0.9rem;
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
    
    span:last-child {
      color: ${({ theme }) => theme.colors.text};
      font-size: 1.1rem;
      font-weight: 600;
    }
  }
`;

const Section = styled.div`
  h2 {
    font-size: 1.5rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 1rem;
  }
  
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    line-height: 1.6;
  }
`;

const ReviewsSection = styled(Section)`
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid ${({ theme }) => theme.colors.border};
`;

const ReviewCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  padding: 1.5rem;
  margin-bottom: 1rem;
`;

const ReviewHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
  
  .reviewer {
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
  }
  
  .date {
    font-size: 0.85rem;
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

const ReviewRating = styled.div`
  display: flex;
  align-items: center;
  gap: 0.25rem;
  color: #f59e0b;
  margin-bottom: 0.75rem;
`;

const DoctorProfile = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [doctor, setDoctor] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const loggedInUser = JSON.parse(localStorage.getItem('user') || '{}');

  useEffect(() => {
    const fetchDoctorAndReviews = async () => {
      try {
        const [docRes, revRes] = await Promise.all([
          doctorService.getById(id),
          reviewService.getByDoctor(id)
        ]);
        setDoctor(docRes.data);
        if (revRes.data && Array.isArray(revRes.data)) {
          // If the backend returns paginated content, handle it:
          setReviews(revRes.data.content || revRes.data);
        }
      } catch (err) {
        console.error("Failed to fetch doctor details", err);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctorAndReviews();
  }, [id]);

  if (loading) return <PageContainer>{t('doctorProfile.loading')}</PageContainer>;
  if (!doctor) return <PageContainer>{t('doctorProfile.notFound')}</PageContainer>;

  const reviewCount = doctor.reviewsCount || 0;
  const avgRating = doctor.averageRating
    ? doctor.averageRating.toFixed(1)
    : 'N/A';

  return (
    <PageContainer>
      <BackButton onClick={() => navigate(-1)}>
        <ChevronLeft size={20} /> {t('doctorProfile.backToSearch')}
      </BackButton>

      <ProfileCard>
        <Avatar>
          {extractAvatar(doctor.professionalDescription).photoUrl ? (
            <img src={extractAvatar(doctor.professionalDescription).photoUrl} alt="Avatar" />
          ) : (
            <>{doctor.firstName?.[0] || 'D'}{doctor.lastName?.[0] || 'R'}</>
          )}
        </Avatar>
        <ProfileInfo>
          <h1>{t('patientDashboard.dr')} {doctor.firstName} {doctor.lastName}</h1>
          <div className="specialty">{doctor.specialization}</div>
          
          <StatsGrid>
            <div>
              <span><Star size={16} color="#f59e0b" /> {t('doctorProfile.rating')}</span>
              <span>{avgRating} {reviewCount > 0 && `(${reviewCount})`}</span>
            </div>
          </StatsGrid>

          {loggedInUser?.role === 'patient' && (
            <Button 
              style={{ alignSelf: 'flex-start', marginTop: '0.5rem', padding: '1rem 2rem' }}
              onClick={() => navigate(`/patient/book/${doctor.id}`, { state: { doctor } })}
            >
              <Calendar size={18} /> {t('doctorProfile.bookAppointment')}
            </Button>
          )}
        </ProfileInfo>
      </ProfileCard>

      <Section>
        <h2>{t('doctorProfile.about')}</h2>
        <p>{extractAvatar(doctor.professionalDescription).cleanBio || t('doctorProfile.noDescription')}</p>
      </Section>

      <ReviewsSection>
        <h2>Patient Reviews</h2>
        {reviews.length === 0 ? (
          <p>No reviews yet for this doctor.</p>
        ) : (
          reviews.map((review, idx) => (
            <ReviewCard key={idx}>
              <ReviewHeader>
                <div className="reviewer">{review.clientName || review.clientFullName || 'Anonymous Patient'}</div>
                <div className="date">
                  {review.createdAt ? new Date(review.createdAt).toLocaleDateString() : ''}
                </div>
              </ReviewHeader>
              <ReviewRating>
                {Array.from({ length: 5 }).map((_, i) => (
                  <Star 
                    key={i} 
                    size={14} 
                    fill={i < (review.rating || 5) ? "#f59e0b" : "transparent"} 
                    color={i < (review.rating || 5) ? "#f59e0b" : "#d1d5db"} 
                  />
                ))}
              </ReviewRating>
              <p>{review.comment}</p>
            </ReviewCard>
          ))
        )}
      </ReviewsSection>
    </PageContainer>
  );
};

export default DoctorProfile;
