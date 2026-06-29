import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Star, MessageSquare } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import { reviewService } from '../../services/api';

const PageContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  padding-bottom: 1rem;
  
  h1 {
    font-size: 1.8rem;
    color: ${({ theme }) => theme.colors.text};
    display: flex;
    align-items: center;
    gap: 0.75rem;
  }
`;

const OverallRating = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  
  .score {
    font-size: 2rem;
    font-weight: 700;
    color: ${({ theme }) => theme.colors.primary};
  }
  .label {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.9rem;
  }
`;

const ReviewList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

const ReviewCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem;
  transition: ${({ theme }) => theme.transitions.default};
  
  &:hover {
    border-color: ${({ theme }) => theme.colors.primary};
    box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
  }
`;

const ReviewHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
`;

const PatientInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  
  .name {
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
    font-size: 1.1rem;
  }
  .date {
    font-size: 0.85rem;
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

const Stars = styled.div`
  display: flex;
  gap: 0.25rem;
  color: #fbbf24;
`;

const ReviewBody = styled.p`
  color: ${({ theme }) => theme.colors.text};
  line-height: 1.6;
  margin: 0;
`;

const DoctorReviews = () => {
  const { t } = useTranslation();
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        const userStr = localStorage.getItem('user');
        if (userStr) {
          const user = JSON.parse(userStr);
          const response = await reviewService.getByDoctor(user.id);
          setReviews(response.data || []);
        }
      } catch (error) {
        console.error("Failed to fetch reviews", error);
      } finally {
        setLoading(false);
      }
    };
    fetchReviews();
  }, []);

  // Calculate average rating
  const avgRating = reviews.length > 0 
    ? reviews.reduce((acc, curr) => acc + curr.rating, 0) / reviews.length 
    : 0;

  return (
    <PageContainer>
      <Header>
        <h1><MessageSquare size={28} /> {t('doctorReviews.title')}</h1>
        <OverallRating>
          <span className="score">{avgRating.toFixed(1)}</span>
          <span className="label">{t('doctorReviews.averageRating')}</span>
        </OverallRating>
      </Header>

      <ReviewList>
        {loading ? (
          <div style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>Loading reviews...</div>
        ) : reviews.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>No reviews yet.</div>
        ) : (
          reviews.map((review) => (
            <ReviewCard key={review.id}>
              <ReviewHeader>
                <PatientInfo>
                  {/* Obfuscate name if anonymous */}
                  <span className="name">
                    {review.isAnonymous ? t('doctorReviews.anonymous') : review.patientName || 'Patient'}
                  </span>
                  <span className="date">{review.createdAt ? new Date(review.createdAt).toLocaleDateString() : review.date || 'Unknown Date'}</span>
                </PatientInfo>
                <Stars>
                  {Array.from({ length: 5 }).map((_, i) => (
                    <Star 
                      key={i} 
                      size={18} 
                      fill={i < review.rating ? "currentColor" : "none"} 
                      color={i < review.rating ? "currentColor" : "#cbd5e1"}
                    />
                  ))}
                </Stars>
              </ReviewHeader>
              <ReviewBody>
                "{review.comment}"
              </ReviewBody>
            </ReviewCard>
          ))
        )}
      </ReviewList>
    </PageContainer>
  );
};

export default DoctorReviews;
