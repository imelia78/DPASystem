import { useState, useMemo, useEffect } from 'react';
import styled from 'styled-components';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { doctorService } from '../services/api';
import { Search, Filter, Star, MessageSquare, Info } from 'lucide-react';
import { Input, Select } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { useTranslation } from 'react-i18next';
import { extractAvatar } from '../utils/avatarUtils';
import { PROCEDURE_TYPES } from '../config/constants';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  
  h1 {
    font-size: 2rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.5rem;
  }
  p {
    color: ${({ theme }) => theme.colors.primary};
    font-weight: 500;
  }
`;

const Avatar = styled.div`
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: ${({ theme }) => theme.colors.surfaceHover};
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.primary};
  margin-bottom: 1rem;
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ControlsContainer = styled.div`
  display: flex;
  gap: 1rem;
  background: ${({ theme }) => theme.colors.surface};
  padding: 1.5rem;
  border-radius: ${({ theme }) => theme.radii.lg};
  border: 1px solid ${({ theme }) => theme.colors.border};
  align-items: center;
  flex-wrap: wrap;

  @media (max-width: 768px) {
    padding: 1rem;
    gap: 0.75rem;
    flex-direction: column;
    align-items: stretch;
  }
`;

const SearchBox = styled.div`
  flex: 1;
  min-width: 200px;
  position: relative;
  
  svg {
    position: absolute;
    left: 1rem;
    top: 50%;
    transform: translateY(-50%);
    color: ${({ theme }) => theme.colors.textMuted};
  }
  
  input {
    padding-left: 3rem;
  }
`;

const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  color: ${({ theme }) => theme.colors.text};
  user-select: none;
  
  input {
    accent-color: ${({ theme }) => theme.colors.primary};
    width: 1.2rem;
    height: 1.2rem;
    cursor: pointer;
  }
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
`;

const DoctorCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1.5rem;
  transition: ${({ theme }) => theme.transitions.default};
  cursor: pointer;

  &:hover {
    box-shadow: ${({ theme }) => theme.shadows.card};
    transform: translateY(-2px);
  }

  @media (max-width: 640px) {
    flex-direction: column;
    align-items: flex-start;
  }
`;

const MainInfo = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 100%;
`;

const HeaderRow = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
`;

const RatingPill = styled.div`
  background: #fef9c3;
  color: #854d0e;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-weight: 600;
  font-size: 0.9rem;
`;

const NameSection = styled.div`
  h3 {
    font-size: 1.3rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.25rem;
    font-weight: 600;
  }
  .specialty {
    color: #2563eb;
    font-size: 0.95rem;
    font-weight: 500;
  }
`;

const BioSnippet = styled.div`
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: 0.9rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
`;

const InfoGrid = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: 0.9rem;

  div {
    display: flex;
    align-items: center;
    gap: 0.4rem;
  }
`;

const ActionRow = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 0.5rem;
  flex-wrap: wrap;
  gap: 1rem;
`;

const AvailableBadge = styled.div`
  background: #dcfce7;
  color: #166534;
  padding: 0.35rem 1rem;
  border-radius: 9999px;
  font-size: 0.85rem;
  font-weight: 600;
  display: inline-block;
  text-align: center;
`;

const parseBackendDate = (dt) => {
  if (!dt) return null;
  if (Array.isArray(dt)) {
    const [y, m, d, h, min] = dt;
    return new Date(y, m - 1, d, h, min);
  }
  return new Date(dt);
};

const DoctorList = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { t } = useTranslation();
  const subtype = searchParams.get('subtype');
  const category = searchParams.get('category');
  const loggedInUser = JSON.parse(localStorage.getItem('user') || '{}');

  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [availableWithin2Weeks, setAvailableWithin2Weeks] = useState(false);
  const [sortOption, setSortOption] = useState('rating');

  useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const response = await doctorService.getAll(100, 0);
        setDoctors(response.data);
      } catch (err) {
        console.error("Failed to fetch doctors", err);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctors();
  }, []);

  const filteredDoctors = useMemo(() => {
    let result = doctors;

    // Filter out the logged-in doctor
    if (loggedInUser.role === 'doctor' && loggedInUser.id) {
      result = result.filter(doc => doc.id !== loggedInUser.id);
    }

    // Filter by procedure subtype or category if specified
    if (subtype) {
      result = result.filter(doc => doc.specialization?.toLowerCase().includes(subtype.toLowerCase()));
    } else if (category) {
      result = result.filter(doc => doc.specialization?.toLowerCase().includes(category.toLowerCase()));
    }

    // Search query filter
    if (searchQuery) {
      const lowerQ = searchQuery.toLowerCase();
      result = result.filter(doc => 
        doc.firstName?.toLowerCase().includes(lowerQ) ||
        doc.lastName?.toLowerCase().includes(lowerQ) ||
        (doc.specialization || '').toLowerCase().includes(lowerQ)
      );
    }

    // Available within 2 weeks filter
    if (availableWithin2Weeks) {
      const now = new Date();
      const twoWeeksFromNow = new Date();
      twoWeeksFromNow.setDate(now.getDate() + 14);

      result = result.filter(doc => {
        // Calculate total capacity
        const specName = (doc.specialization || '').toUpperCase().replace(/\s+/g, '_');
        const spec = PROCEDURE_TYPES.find(p => p.category.toUpperCase().replace(/\s+/g, '_') === specName);
        let maxDur = 30;
        if (spec && spec.subtypes && spec.subtypes.length > 0) {
          maxDur = Math.max(...spec.subtypes.map(s => s.duration));
        }
        const interval = maxDur + 15;
        const slotsPerDay = Math.floor((17 * 60 - 9 * 60) / interval);
        const totalCapacity = slotsPerDay * 14;

        // Count booked slots in next 14 days
        let bookedCount = 0;
        if (doc.appointments) {
          doc.appointments.forEach(app => {
            if (app.appointmentStatus === 'REJECTED' || app.appointmentStatus === 'CANCELLED') return;
            const d = parseBackendDate(app.appointmentDateTime);
            if (d && d >= now && d <= twoWeeksFromNow) {
              bookedCount++;
            }
          });
        }
        return bookedCount < totalCapacity;
      });
    }

    // Sorting
    result.sort((a, b) => {
      if (sortOption === 'rating') {
        const avgA = a.averageRating || 0;
        const avgB = b.averageRating || 0;
        return avgB - avgA;
      }
      if (sortOption === 'name-asc') return (a.lastName || '').localeCompare(b.lastName || '');
      if (sortOption === 'name-desc') return (b.lastName || '').localeCompare(a.lastName || '');
      return 0;
    });

    return result;
  }, [doctors, subtype, category, searchQuery, availableWithin2Weeks, sortOption]);

  const handleDoctorClick = (docId) => {
    if (loggedInUser?.role === 'doctor') {
      navigate(`/doctor/view-profile/${docId}`);
    } else {
      navigate(`/patient/doctor/${docId}`);
    }
  };

  return (
    <PageContainer>
      <Header>
        <div>
          <h1>{t('doctorList.title')}</h1>
          {(subtype || category) && <p>{t('doctorList.showingSpecialists')} {subtype || category}</p>}
        </div>
      </Header>

      <ControlsContainer>
        <SearchBox>
          <Search size={20} />
          <Input 
            placeholder={t('doctorList.searchPlaceholder')} 
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </SearchBox>
        
        <CheckboxLabel>
          <input 
            type="checkbox" 
            checked={availableWithin2Weeks}
            onChange={(e) => setAvailableWithin2Weeks(e.target.checked)}
          />
          {t('doctorList.availableWithin')}
        </CheckboxLabel>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', flexWrap: 'wrap' }}>
          <Filter size={20} color="#94a3b8" />
          <Select 
            style={{ width: '100%', maxWidth: '200px' }} 
            value={sortOption}
            onChange={(e) => setSortOption(e.target.value)}
          >
            <option value="rating">{t('doctorList.sortRating')}</option>
            <option value="name-asc">{t('doctorList.sortNameAsc')}</option>
            <option value="name-desc">{t('doctorList.sortNameDesc')}</option>
          </Select>
        </div>
      </ControlsContainer>

      {loading ? (
        <div style={{ padding: '2rem', textAlign: 'center', color: '#94a3b8' }}>{t('doctorList.loading')}</div>
      ) : (
        <Grid>
          {filteredDoctors.length === 0 ? (
            <div style={{ padding: '2rem', textAlign: 'center', color: '#94a3b8', gridColumn: '1 / -1' }}>
              {t('doctorList.noDoctorsFound')}
            </div>
          ) : (
            filteredDoctors.map(doctor => {
              const reviewCount = doctor.reviewsCount || 0;
              const avgRating = doctor.averageRating 
                ? doctor.averageRating.toFixed(1) 
                : t('doctorList.new');

              return (
                <DoctorCard key={doctor.id} onClick={() => handleDoctorClick(doctor.id)}>
                  <Avatar style={{ marginBottom: 0, alignSelf: 'flex-start' }}>
                    {extractAvatar(doctor.professionalDescription).photoUrl ? (
                      <img src={extractAvatar(doctor.professionalDescription).photoUrl} alt="Avatar" />
                    ) : (
                      <>{doctor.firstName?.[0] || 'D'}{doctor.lastName?.[0] || 'R'}</>
                    )}
                  </Avatar>

                  <MainInfo>
                    <HeaderRow>
                      <NameSection>
                        <h3>{t('patientDashboard.dr')} {doctor.firstName} {doctor.lastName}</h3>
                        <div className="specialty">{doctor.specialization}</div>
                      </NameSection>
                      
                      <RatingPill>
                        <Star size={16} fill="#eab308" color="#eab308" />
                        {avgRating}
                      </RatingPill>
                    </HeaderRow>

                    <BioSnippet>
                      {extractAvatar(doctor.professionalDescription).cleanBio}
                    </BioSnippet>

                    <ActionRow>
                      <InfoGrid>
                        <div><MessageSquare size={16} /> {reviewCount} patient reviews</div>
                      </InfoGrid>

                      <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                        <AvailableBadge>Available</AvailableBadge>
                        {loggedInUser?.role === 'patient' && (
                          <Button onClick={(e) => { e.stopPropagation(); navigate(`/patient/book/${doctor.id}`, { state: { doctor } }); }} style={{ background: '#2563eb', color: 'white', padding: '0.5rem 1.5rem' }}>
                            Book Appointment
                          </Button>
                        )}
                      </div>
                    </ActionRow>
                  </MainInfo>
                </DoctorCard>
              );
            })
          )}
        </Grid>
      )}
    </PageContainer>
  );
};

export default DoctorList;
